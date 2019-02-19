![alt PROJECTIONS banner](doc/images/viper-wide-banner.jpg)

# PROJECTIONS  

PROJECTIONS is a java library which implements calculations for mapping applications. 

* [Getting Started](#getting-started) Download, install, and build the jar file.
* [API](https://github.com/vipersoftwareservices/projections/doc/api/index.html) API documentation(javadocs)
* [Binary Download](http://www.tnevin.com/projections/) Pre-built projections jar file
* [Examples](http://www.tnevin.com/projections/) View the projections
* [Authors Home Page](http://www.tnevin.com) In progress

## Features

The following is a list of the mapping projections provided:
1. Albers
2. Azimuthal
3. Bonne
4. Cassini
5. Cylindrical Equal Area
6. Equidistant Conic
7. Equidistant Cylindrical
8. Gnomonic
9. Lambert Conformal Conic
10. Lambert Equal Area
11. Mercator
12. Miller
13. Orthographic
14. Polyconic
15. Space Oblique Mercator (in Progress)
16. Stereographic
17. Transverse Mercator
18. Universal Transverse Mercator
19. Vertical Perspective
  
## Getting Started

These instructions will get you a copy of the project up and running on your local machine.

### Prerequisites

What things you need to install the software and how to install them

```
* java 1.6 or better.
* ant 1.9 or better (optional).
* For Windows, install CygWin, latest.
```

Note: ant commands have been run and tested using cygwin bash shell. Dos shell, and other linux shells will probably work.

### Installing  

1. Download the PROJECTIONS zip file, and unzip it.

```
https://github.com/vipersoftwareservices/projections
```

2. Run the build script if building sources is desired, runtime jars are available.

```
ant clean all
```
 

3. Run the tests, by running ant command.

```
ant test
```

4. View the JUnit test results, by bringing the following file up in browser.
For windows, double click the file in the disk explorer, the location of the file is:

```
<install-directory>/build/reports/index.html
```

5. View the code coverage file in the browser..
For windows, double click the file in the disk explorer, the location of the file is:

```
<install-directory>/build/jacoco/index.html
```
 
6. Find the binary jar file in the build directory and use


```
<install-directory>/build/world.jar
```
 

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/vipersoftwareservices/projections) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

In progress

## Authors

* **Tom Nevin** - *Initial work* - [PROJECTIONS](https://github.com/vipersoftwareservices/projections)

See also the list of [contributors](https://github.com/vipersoftwareservices/projections/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details

 
