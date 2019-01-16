BEGIN {
	print "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	print "<!DOCTYPE WorldData SYSTEM \"../world.dtd\">";
	print "";
	print "<WorldData>";
	print "";
	print "<Heading>";
	print "";
	print "	This file contains points (lats-lons) for the world map.";
	print "";
	print "	Athor: TomNevin@pacbell.net";
	print "	Date:  04/18/2003  - ";
	print "";
	print "</Heading>";

	inSegment = "false";
}

{

if ($1 == "segment") {

	if (inSegment == "true") {
		print "</Segment>";
	}
	inSegment = "true";

	print "<Segment number=\"" $2 "\" rank=\"" $3 "\" kind=\"land\">";
	print "<Grid min_lat=\"-90.0\" max_lat=\"90.0\" min_lon=\"-180.0\" max_lon=\"180.0\"/>";
	print "";

} else {

	print "<Pt lat=\"" $1 "\" lon=\"" $2 "\" />";
}
}

END {
	if (inSegment == "true") {
		print "</Segment>";
	}
	print "</WorldData>";
}
