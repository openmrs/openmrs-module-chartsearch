<%
    ui.decorateWith("appui", "standardEmrPage")
%>


Hello, here you are; on the Main Page where all chartsearch links/launches will be added.
<br/>
<b>ManageChartSearch will address several functionalities as mentioned at: <a href="https://notes.openmrs.org/ChartSearch-Module-SupportOtherModulesSearches">Support Other Modules Searches</a></b>
<br /><br />

<div class="ui-tabs">
	<ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" role="tablist">
	    <li class="ui-state-default ui-corner-top ui-tabs-active ui-state-active">
	    	<a class="ui-tabs-anchor">
	        	Custom Indexing
	        </a>
	    </li>
	    <li class="ui-state-default ui-corner-top" >
	        <a class="ui-tabs-anchor">
	             Non-Patient Data Searches
	        </a>
	    </li>
	    <li class="ui-state-default ui-corner-top" >
	        <a class="ui-tabs-anchor">
	             Manage Synonym groups
	        </a>
	    </li>
	    <li class="ui-state-default ui-corner-top" >
	        <a class="ui-tabs-anchor">
	             Manage Filters
	        </a>
	    </li>
	    <li class="ui-state-default ui-corner-top" >
	        <a class="ui-tabs-anchor">
	             Settings
	        </a>
	    </li>
    </ul>
    <div class="ui-tabs-panel ui-widget-content ui-corner-bottom">
         <p>Tabs content</p>
    </div>
</div>


<div id="tabs">
  <ul>
    <li><a href="#fragment-1"><span>One</span></a></li>
    <li><a href="#fragment-2"><span>Two</span></a></li>
    <li><a href="#fragment-3"><span>Three</span></a></li>
  </ul>
  <div id="fragment-1">
    <p>First tab is active by default:</p>
    <pre><code>$( "#tabs" ).tabs(); </code></pre>
  </div>
  <div id="fragment-2">
    Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.
    Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.
  </div>
  <div id="fragment-3">
    Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.
    Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.
    Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.
  </div>
</div>

