/*
 * @(#)Filename.java	1.00 2002/07/15
 *
 * Copyright 1998-2002 by Viper Software Services
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Viper Software Services. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Viper Software Services.
 *
 * @Author Tom Nevin (TomNevin@pacbell.net)
 */

package com.viper.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.viper.projections.Grid;
import com.viper.projections.MapPoint;
import com.viper.projections.MapProjection;
import com.viper.world.model.Pt;
import com.viper.world.model.Segment;
import com.viper.world.model.WorldData;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class WorldParser extends Application {

    private MapProjection projection = MapProjection.getInstance("Passthru");
    private Grid worldXY = new Grid(-180.0, 180.0, -180.0, 180.0);
    private Grid world = new Grid(-180.0, 180.0, -180.0, 180.0);
    private String xmlfilename = "results/world-60.xml";
    private int width = 500;
    private int height = 500;

    MapPanel mapPane = null;
    TextField radiusTF = new TextField();
    TextField eccTF = new TextField();
    TextField latOriginTF = new TextField();
    TextField lonOriginTF = new TextField();
    TextField sp1TF = new TextField();
    TextField sp2TF = new TextField();

    Label latWorld = new Label("XX-XX-XX XX-XX-XX");
    Label lonWorld = new Label("XX-XX-XX XX-XX-XX");
    Label latView = new Label("XX-XX-XX XX-XX-XX");
    Label lonView = new Label("XX-XX-XX XX-XX-XX");

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("World Viewer");

        mapPane = new MapPanel(convert(xmlfilename), this, width, height);

        BorderPane centerPane = new BorderPane();
        centerPane.setCenter(mapPane);
        centerPane.setRight(createControlPane());

        VBox root = new VBox();
        root.getChildren().addAll(createMenuPane(), createToolPane(), centerPane);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public WorldData convert(String filename) {

        WorldData data = null;
        try {

            data = JAXBUtils.getObject(WorldData.class, new File(filename));

        } catch (Exception e) {
            System.err.println("ERROR: " + e);
            System.err.println("     : xmlFilename=" + filename);
        }
        return data;
    }

    private double toDouble(String str) {
        if (str == null || str.length() == 0) {
            return 0.0;
        }

        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            System.err.println("toDouble: " + e + ": " + str);
        }
        return 0.0;
    }

    private int toInt(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }

        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            System.err.println("toInt: " + e + ": " + str);
        }
        return 0;
    }

    // --------------------------------------------------------------------
    public void plotVector(GraphicsContext gc, Grid world, int width, int height, List<MapPoint> pts, boolean isLine) {

        if (pts == null || pts.size() == 0) {
            return;
        }

        double sx = (double) width / (world.getEast() - world.getWest());
        double sy = (double) height / (world.getNorth() - world.getSouth());

        MapPoint pp1 = new MapPoint();

        boolean isFirst = true;

        for (int i = 0; i < pts.size(); i++) {
            MapPoint pt = new MapPoint(pts.get(i).getLat(), pts.get(i).getLon());
            if (projection.inView(pt) == false)
                continue;

            projection.toProjection(pt, pp1);

            int x1 = (int) ((pp1.lon - world.getWest()) * sx);
            int y1 = (int) ((pp1.lat - world.getSouth()) * sy);

            if (isFirst == true) {
                isFirst = false;
                gc.beginPath();
                gc.moveTo(x1, height - y1);
            } else {
                gc.lineTo(x1, height - y1);
            }
            // System.err.println("plotVector (x/y)=" + i + "(" + x1 + "," + y1 + "," + sx + "/" +
            // sy + "," + width + "," + height + "," + isFirst);
        }
        if (!isFirst) {
            gc.closePath();
            if (isLine) {
                gc.stroke();
            } else {
                gc.fill();
            }
        }
    }

    public List<MapPoint> toMapPoints(List<Pt> pts) {

        List<MapPoint> list = new ArrayList<MapPoint>();
        for (Pt pt : pts) {
            list.add(new MapPoint(pt.getLat(), pt.getLon()));
        }
        return list;
    }

    // --------------------------------------------------------------------
    public void plotSegments(GraphicsContext gc, Grid world, int width, int height, List<Segment> segments) {
        for (Segment segment : segments) {
            gc.setFill(Color.GREEN);
            plotVector(gc, world, width, height, toMapPoints(segment.getPt()), false);
        }
    }

    public void maxWorld(Grid world, List<Segment> segments) {
        for (Segment s : segments) {
            for (Pt pt1 : s.getPt()) {
                world.grow(pt1.getLat(), pt1.getLon());
            }
        }
    }

    public MapProjection getProjection() {
        return this.projection;
    }

    public Pane createToolPane() {
        return new Pane();
    }

    public MenuBar createMenuPane() {
        // Menus
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        // fileMenu.setMnemonic(getMnemonic("FileMenu.file_mnemonic"));
        // fileMenu.getAccessibleContext().setAccessibleDescription(getString("FileMenu.accessible_description"));
        fileMenu.getItems().add(createMenuItem("Import...", null, null, null));
        fileMenu.getItems().add(createMenuItem("Export...", null, null, null));
        // fileMenu.getItems().add(new Separator());
        fileMenu.getItems().add(createMenuItem("Exit", null, new ExitListener(), null));
        menuBar.getMenus().add(fileMenu);

        Menu editMenu = new Menu("Edit");
        menuBar.getMenus().add(editMenu);

        editMenu.getItems().add(createMenuItem("Cut", "X", null, null));
        editMenu.getItems().add(createMenuItem("Copy", "C", null, null));
        editMenu.getItems().add(createMenuItem("Paste", "V", null, null));
        editMenu.getItems().add(createMenuItem("Paste Special...", null, null, null));
        editMenu.getItems().add(createMenuItem("Select All", "A", null, null));
        // editMenu.getItems().add(new Separator());
        editMenu.getItems().add(createMenuItem("Find..", "F", null, null));

        Menu viewMenu = new Menu("View");
        menuBar.getMenus().add(viewMenu);

        return menuBar;
    }

    private MenuItem createMenuItem(String label, String key, EventHandler<ActionEvent> listener, String tooltip) {
        MenuItem menuItem = new MenuItem(label);
        if (key != null) {
            menuItem.setAccelerator(KeyCombination.keyCombination(key));
        }
        if (tooltip != null) {
            // menuItem.setToolTipText(tooltip);
        }
        if (listener != null) {
            menuItem.setOnAction(listener);
        }
        return menuItem;
    }

    public VBox createControlPane() {

        VBox pane = new VBox();
        pane.getChildren().add(new ProjectionChoice());

        pane.getChildren().add(new Label("World"));
        pane.getChildren().add(latWorld);
        pane.getChildren().add(lonWorld);

        pane.getChildren().add(new Label("View"));
        pane.getChildren().add(latView);
        pane.getChildren().add(lonView);

        TilePane grid = new TilePane();
        grid.setPrefColumns(2);
        grid.getChildren().add(new Label("Radius:"));
        grid.getChildren().add(radiusTF);
        grid.getChildren().add(new Label("Eccentricity:"));
        grid.getChildren().add(eccTF);
        grid.getChildren().add(new Label("SP1:"));
        grid.getChildren().add(sp1TF);
        grid.getChildren().add(new Label("SP2:"));
        grid.getChildren().add(sp2TF);
        pane.getChildren().add(grid);

        pane.getChildren().add(new Label("Origin:"));
        pane.getChildren().add(latOriginTF);
        pane.getChildren().add(lonOriginTF);

        Button plotBtn = new Button("Plot");
        plotBtn.setOnAction(new PlotHandler());
        pane.getChildren().add(plotBtn);

        return pane;
    }

    public int fix(double d) {
        return (int) (d - (d % 1.0));
    }

    private String getLatText(double lat) {
        String dir = (lat < 0.0) ? "S" : "N";

        lat = (lat < 0.0) ? -lat : lat;

        int deg = fix(lat);
        int min = fix((lat % 1.0) * 60.0);
        int sec = fix((lat % (1.0 / 60.0)) * 3600.0);

        return deg + "." + min + "." + sec + dir;
    }

    private String getLonText(double lon) {
        String dir = (lon < 0.0) ? "W" : "E";

        lon = (lon < 0.0) ? -lon : lon;

        int deg = fix(lon);
        int min = fix((lon % 1.0) * 60.0);
        int sec = fix((lon % (1.0 / 60.0)) * 3600.0);

        return deg + "." + min + "." + sec + dir;
    }

    public void refresh() {

        MapProjection MP = getProjection();

        latWorld.setText(getLatText(world.getNorth()) + " " + getLatText(world.getSouth()));
        lonWorld.setText(getLonText(world.getEast()) + " " + getLonText(world.getWest()));
        latView.setText(getLatText(worldXY.getNorth()) + " " + getLatText(worldXY.getSouth()));
        lonView.setText(getLonText(worldXY.getEast()) + " " + getLonText(worldXY.getWest()));

        sp1TF.setText("" + MP.getParallel1());
        sp2TF.setText("" + MP.getParallel2());

        latOriginTF.setText("" + MP.getOriginLat());
        lonOriginTF.setText("" + MP.getOriginLon());

        radiusTF.setText("" + MP.getRadius());
        eccTF.setText("" + MP.getEccentricity());

        mapPane.repaint();
    }

    public class PlotHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {

            System.err.println("Button: actionPerformed.");
            MapProjection MP = getProjection();

            MP.setRadius(toDouble(radiusTF.getText()));
            MP.setEccentricity(toDouble(eccTF.getText()));
            MP.setParallel1(toDouble(sp1TF.getText()));
            MP.setParallel2(toDouble(sp2TF.getText()));

            MP.setOriginLat(toDouble(latOriginTF.getText()));
            MP.setOriginLon(toDouble(lonOriginTF.getText()));

            refresh();
        }
    }

    public class ProjectionChoice extends ChoiceBox<String> implements EventHandler<ActionEvent> {
        public ProjectionChoice() {
            super();
            getItems().add("Albers");
            getItems().add("Azimuthal");
            getItems().add("Bonne");
            getItems().add("Cassini");
            getItems().add("CylindricalEqualArea");
            getItems().add("EquidistantConic");
            getItems().add("EquidistantCylindrical");
            getItems().add("Gnomonic");
            getItems().add("LambertConformalConic");
            getItems().add("LambertEqualArea");
            getItems().add("Mercator");
            getItems().add("Miller");
            getItems().add("Orthographic");
            getItems().add("Passthru");
            getItems().add("Polyconic");
            getItems().add("SpaceObliqueMercator");
            getItems().add("Stereographic");
            getItems().add("TransverseMercator");
            getItems().add("UTM");
            getItems().add("VerticalPerspective");

            setOnAction(this);
        }

        /**
         * Invoked when an item has been selected or deselected by the user. The code written for
         * this method performs the operations that need to occur when an item is selected (or
         * deselected).
         */
        public void handle(ActionEvent event) {

            ProjectionChoice item = (ProjectionChoice) event.getSource();

            projection = MapProjection.getInstance(item.getSelectionModel().getSelectedItem());
        }
    }

    public class MapPanel extends Canvas {

        WorldData wd = null;
        WorldParser parser = null;

        public MapPanel(WorldData wd, WorldParser parser, int width, int height) {
            super();

            this.parser = parser;
            this.wd = wd;

            setWidth(width);
            setHeight(height);
        }

        public void paintGrid(GraphicsContext gc, Grid worldXY, Grid world, int width, int height) {

            final int npts = 20;
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1.0);

            double dx = (world.getEast() - world.getWest()) / 10.0;
            double dy = (world.getNorth() - world.getSouth()) / 10.0;

            List<MapPoint> pts = new ArrayList<MapPoint>();
            MapPoint pt1 = new MapPoint();
            MapPoint pt2 = new MapPoint();

            MapProjection MP = parser.getProjection();

            for (double x = world.getWest(); x <= world.getEast(); x = x + dx) {
                for (double y = world.getSouth(); y < (world.getNorth() - dy / 2.0); y = y + dy) {
                    pt1.lat = y;
                    pt1.lon = x;
                    pt2.lat = y + dy;
                    pt2.lon = x;

                    System.err.println("paintGrid1: " + pt1 + "," + pt2 + "," + width + "," + height);

                    MP.computeLine(pts, pt1, pt2, npts);
                    parser.plotVector(gc, worldXY, width, height, pts, true);
                }
            }

            for (double y = world.getSouth(); y <= world.getNorth(); y = y + dy) {
                for (double x = world.getWest(); x < (world.getEast() - dx / 2.0); x = x + dx) {

                    pt1.lat = y;
                    pt1.lon = x;
                    pt2.lat = y;
                    pt2.lon = x + dx;

                    System.err.println("paintGrid2: " + pt1 + "," + pt2 + "," + width + "," + height);

                    MP.computeLine(pts, pt1, pt2, npts);
                    parser.plotVector(gc, worldXY, width, height, pts, true);
                }
            }
        }

        public void repaint() {
            GraphicsContext gc = getGraphicsContext2D();

            System.err.println("Paint: " + gc);

            gc.setStroke(Color.BLACK);
            gc.setFill(Color.BLUE);

            int width = (int) getWidth();
            int height = (int) getHeight();
            gc.fillRect(0, 0, width, height);

            world.reset(-180.0, 180.0, -180.0, 180.0);
            parser.maxWorld(world, wd.getSegment());

            System.err.println("Paint: max world :" + world);

            projection.toProjection(world, worldXY);

            System.err.println("Paint: worldXY :" + worldXY + "'" + width + "/" + height);

            gc.setFill(Color.RED);
            paintGrid(gc, worldXY, world, width, height);

            parser.plotSegments(gc, worldXY, width, height, wd.getSegment());

        }
    }

    // ---------------------------------------------------------------------------------
    // Listeners
    // ---------------------------------------------------------------------------------

    class ExitListener implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
            // TODO - check if any outstanding edits.
            System.exit(0);
        }
    }

    public static <T> StringProperty buildStringProperty(T bean, String propertyName) {
        try {
            return JavaBeanStringPropertyBuilder.create().bean(bean).name(propertyName).build();
        } catch (Exception ex) {
            System.err.println("buildStringProperty: No Such Method: " + bean + ", " + propertyName);
        }
        return null;
    }

    // ---------------------------------------------------------------------------------
    // MAIN
    // ---------------------------------------------------------------------------------
    public static void main(String args[]) {

        WorldParser parser = new WorldParser();

        for (int i = 0; i < args.length; i++) {
            if ("-xml".equalsIgnoreCase(args[i])) {
                parser.xmlfilename = args[++i];
            } else if ("-width".equalsIgnoreCase(args[i])) {
                parser.width = parser.toInt(args[++i]);
            } else if ("-height".equalsIgnoreCase(args[i])) {
                parser.height = parser.toInt(args[++i]);
            } else if ("-projection".equalsIgnoreCase(args[i])) {
                parser.projection = MapProjection.getInstance(args[++i]);

            }
        }
        launch(args);
    }
}
