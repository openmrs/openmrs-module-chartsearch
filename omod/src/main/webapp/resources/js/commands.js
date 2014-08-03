$j(document).ready(function() {
	console.log("Document is ready");
	$j('#patientInfoBtn').click(getPatientInfo);
	$j('#statisticsBtn').click(getStatistics);
	$j('#clearBtn').click(clearIndex);
	$j('#startDaemonBtn').click(changeDaemonsCount);
    $j('#deleteSynGrpBtn').click(deleteSynonymGroup);
});

function getPatientInfo() {
	DWRCommands.getPatientInfo($j("#patientId").val(), function(patientInfo) {
		var text;
		if (!patientInfo)
			text = "Patient is not in the index";
		else {
			var time = patientInfo.lastIndexTime.toString();
			text = "Last indexed time: " + time;
		}
		$j("#patientInfoResult").text(text);
	});
}

function getStatistics() {
	DWRCommands
			.getStatistics(function(stats) {
				if (!stats)
					$j("#statisticsResult").text("Error on getting statistics");
				else {
					$j("#statisticsResult").empty();
					var strategyName = stats.strategyName.toString();
					$j("<div/>", {
						text : "Strategy: " + strategyName
					}).appendTo("#statisticsResult");
					var pruneCount = stats.pruneCount;
					$j("<div/>", {
						text : "Prune patients count: " + pruneCount
					}).appendTo("#statisticsResult");
					$j("<div/>", {
						id : "daemonStates",
						text : "Daemon states:"
					}).appendTo("#statisticsResult");
					for ( var i = 0; i < stats.daemonStates.length; i++) {
						var daemonId = stats.daemonStates[i]['daemon id'];
						var daemonStatus = stats.daemonStates[i]['daemon status'];
						var daemonSuccessCount = stats.daemonStates[i]['daemon success count'];
						var daemonFailCount = stats.daemonStates[i]['daemon fail count'];
						$j(
								"<div/>",
								{
									id : "daemonState" + i,
									text : "Daemon Id: " + daemonId + ", "
											+ "Status:" + daemonStatus + ", "
											+ "Processed:" + daemonSuccessCount
								}).appendTo("#daemonStates");
					}
				}
			});

}

function clearIndex() {
	DWRCommands.clearIndex($j("#clearStrategy").val(), $j("#patientIds").val(),
			$j("#maxPatients").val(), $j("#ago").val(), function(pruneCount) {
				console.log("Begin clearIndex");
				console.log("pruneCount: " + pruneCount);
				if (pruneCount == null)
					$j("#clearResult").text("Error on pruning patients");
				else
					$j("#clearResult").text(
							"Sent request to delete " + pruneCount
									+ " patient(s)");
				console.log("End clearIndex");
			});
}

function changeDaemonsCount() {
	DWRCommands.changeDaemonsCount($j("#daemonsCount").val(), function(daemonsCount) {	
		var text;
		if (daemonsCount < 0)
			text = "Failed to change daemons count";
		else {
			text = "Daemons count successfully changes. New daemons count: " + daemonsCount;
		}
		$j("#daemonsManagementResult").text(text);
	});
}