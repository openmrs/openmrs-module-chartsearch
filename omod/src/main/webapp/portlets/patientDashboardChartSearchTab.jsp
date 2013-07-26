<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:htmlInclude file="/dwr/interface/DWRChartSearchService.js"/>
<openmrs:htmlInclude file="/scripts/jquery/dataTables/css/dataTables_jui.css"/>
<openmrs:htmlInclude file="/scripts/jquery/dataTables/js/jquery.dataTables.min.js"/>
<openmrs:htmlInclude file="/scripts/jquery-ui/js/openmrsSearch.js" />
<openmrs:htmlInclude file="/moduleResources/chartsearch/css/chartsearch.css"/>

<script type="text/javascript">
	var lastSearch;
	$j(document).ready(function() {
		new OpenmrsSearch("patientChartWidget", false, doObsSearch, doSelectionHandler, 
				[{fieldName:"obsId", header:" "}, {fieldName:"obsDate", header:" "}],
				{searchLabel: '',
                    searchPlaceholder:'',
					//includeVoidedLabel: '<spring:message code="SearchResults.includeRetired" javaScriptEscape="true"/>', 
					columnRenderers: [nameColumnRenderer, null], 
					columnVisibility: [true, false],
					searchPhrase:'<request:parameter name="searchPhrase"/>',
					showIncludeVerbose: false
				});
	});
	
	function doSelectionHandler(index, data) {
		renderDetails(data);		
		//alert("admin/observations/obs.form?obsId=" + data.obsId + "&=searchPhrase=" + lastSearch);
	}
	
	
	//searchHandler
	function doObsSearch(text, resultHandler, getMatchCount, opts) {
		lastSearch = text;
		DWRChartSearchService.findObsAndCount(${model.patient.patientId}, text, opts.includeVoided, null, null, null, null, opts.start, opts.length, getMatchCount, resultHandler);
	}
	
	//custom render, appends an arrow and preferredName it exists
	function nameColumnRenderer(oObj){
		if(oObj.aData[1] && $j.trim(oObj.aData[1]) != '')
			return "<span>"+oObj.aData[0]+"</span><span class='otherHit'> &rArr; "+oObj.aData[1]+"</span>";
		
		return "<span>"+oObj.aData[0]+"</span>";
	}
	
	function renderDetails(data){	
		//todo method in DWRChartSearchService
		jQuery("#chartSearchDetails").html(data.obsId + "<br/>" + data.obsDate);
	}
	
</script>

<openmrs:hasPrivilege privilege="Patient Dashboard - View Chart Search Section">

<div id="chartSearchWrapper">
	<div id="chartSearchResults">
		<b class="boxHeader"><spring:message code="Obs.find"/></b>
		<div class="box">
			<div class="searchWidgetContainer" id="patientChartWidget"></div>			
		</div>
	</div>
	
	<div id="chartSearchDetails"></div>
</div>
	
</openmrs:hasPrivilege>