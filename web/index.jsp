<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="author" content="Matthias Pueski">
<meta name="publisher" content="Matthias Pueski">
<meta name="copyright" content="Matthias Pueski">
<meta name="description" content="BlackBoard breadboard designer">
<meta name="keywords" content="Breadboard, Soldering, Prototype, Wiring, Electronics">
<meta name="page-topic" content="Electronics">
<meta name="page-type" content="Private">
<meta name="audience" content="all">
<meta http-equiv="content-language" content="en">
<meta name="robots" content="index, follow">
<meta name="DC.Creator" content="Matthias Pueski">
<meta name="DC.Publisher" content="Matthias Pueski">
<meta name="DC.Rights" content="Matthias Pueski">
<meta name="DC.Description" content="BlackBoard breadboard designer">
<meta name="DC.Language" content="en">
<title>Blackboard breadboard designer</title>
</head>
<body style="font-family: sans-serif;width:1024px">
	<h1>BlackBoard breadboard designer</h1>
	<p>
		This is the home of blackboard, a design tool for so called breadboards.
	</p>
	<p>
		BlackBoard is a personal project of mine and was initially designed to help me
		create electric circuits on a solderable breadboard. 
	</p>
	<p>
		Currently BlackBoard is in an early development stage an I decided to release the
		tool under the GNU Public License V2.
	</p>
	<h2>Reqirements</h2>
	<ul type="square">
		<li>
			Either Linux, Windows or any other platform suitable for running the Java Virtual Machine		
		</li>
		<li>
			Java Runtime 1.6+
		</li>
		<li>
			256MB free memory for applications
		</li>
	</ul>	
	<h2>Installation</h2>
	<p>
		Simply <a href="./download/blackboard-bin-0.1.zip">download</a> the binary distribution, extract it an run <i><b>java -jar BlackBoard.jar</b></i>
	</p>
	<h2>
		Documentation
	</h2>
	<p>The current documentation can be found <a href="docs/index.jsp" target="_blank">here</a></p>	
	<h2>
		Screenshots
	</h2>
	<table border="0" cellpadding="5px" cellspacing="5px">
		<tr>
			<td>
				<a href="./images/blackboard1.png" target="_blank"><img src="./images/blackboard1_small.png" border="0"/></a>
			</td>
			<td>
				<a href="./images/blackboard2.png" target="_blank"><img src="./images/blackboard2_small.png" border="0"/></a>
			</td>
			<td>
				<a href="./images/blackboard3.png" target="_blank"><img src="./images/blackboard3_small.png" border="0"/></a>
			</td>			
		</tr>
	</table>
	<h2>
		<a name="download">Download</a>
	</h2>
	<p>
		<a href="./download/blackboard-bin-0.1.zip">BlackBoard 0.1 executable (~6.5MB)</a>
	</p>
	<h1>Known Bugs</h1>
	<p>
		<ul>
			<li>
				Parts that are created after another sit on top. This is especially a problem when adding IC packages
	   		        and a IC socket after that. In this case the IC is located below the socket. 
			</li>
			<li>When "Snap to grid" is enabled and a wire is drawn, the start of the line might snap in a few pixels away from the mouse.</li>
			<li>
				Other than that: Lots of, since it is an early available prototype, so please don't ask for support and do not make me responsibe for any 
				harmful things that might happen to you while using this program. ;)
			</li>
		</ul>
	</p>
	
	<h1>Author</h1>
	<p>
		<a href="http://www.pueski.de" target="_blank">Matthias Pueski</a>
	</p>
	<h1>License</h1>
	<p>
		All source code released under the GNU Public License V2
	</p>
	<p>
		<i>Last change : 05-29-11 by Matthias Pueski</i>
	</p>
	<p>
		&nbsp;&nbsp;
	</p>
</body>
</html>
