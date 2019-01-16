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

	rank = "?";
}

{

if ($1 == "segment") {

	if (rank == "1") {
		print "</Segment>";
	}

	rank = $4;

	if (rank == "1") {

		print "<Segment number=\"" $2 "\" rank=\"" $4 "\" kind=\"land\">";
		print "<Grid min_lat=\"-90.0\" max_lat=\"90.0\" min_lon=\"-180.0\" max_lon=\"180.0\"/>";
		print "";
	}

} else if (rank == "1") {

	print "<Pt lat=\"" $1 "\" lon=\"" $2 "\" />";
}
}

END {
	if (rank == "1") {
		print "</Segment>";
	}
	print "</WorldData>";
}
