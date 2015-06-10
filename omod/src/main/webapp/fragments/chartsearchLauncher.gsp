<h1>Select patient to launch their chart search page</h1>



${ ui.includeFragment("coreapps", "patientsearch/patientSearchWidget",
	[ afterSelectedUrl: '/chartsearch/chartsearch.page?patientId={{patientId}}',
	showLastViewedPatients: true,
])}
