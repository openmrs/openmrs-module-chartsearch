<%@ include file="/WEB-INF/template/include.jsp" %>

<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
<openmrs:htmlInclude file="/moduleResources/chartsearch/js/main.js"/>
<openmrs:htmlInclude file="/moduleResources/chartsearch/js/Core.js"/>
<openmrs:htmlInclude file="/moduleResources/chartsearch/js/AbstractManager.js"/>
<openmrs:htmlInclude file="/moduleResources/chartsearch/js/Manager.jquery.js"/>
<openmrs:htmlInclude file="/moduleResources/chartsearch/js/Parameter.js"/>
<openmrs:htmlInclude file="/moduleResources/chartsearch/js/ParameterStore.js"/>
<openmrs:htmlInclude file="/moduleResources/chartsearch/js/AbstractWidget.js"/>
<openmrs:htmlInclude file="/moduleResources/chartsearch/js/ResultWidget.2.0.js"/>

<h1>Chart Search Tab</h1>


  <div id="wrap"> 
    <div class="right">
      <div id="result">
        <div id="navigation">
          <ul id="pager"></ul>
          <div id="pager-header"></div>
        </div>
        <div id="docs"></div>
      </div>
    </div>

   
${model.solrResponse}