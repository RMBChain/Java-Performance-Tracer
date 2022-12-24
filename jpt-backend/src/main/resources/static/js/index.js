$(document).ready(function(){
	//method Hierarchy
	var methodHierarchyColumns = [[
        {title:'Thread',        field:'threadId',   width:40, align:'right'},
        {title:'Serial',        field:'serial',     width:40, align:'right'},
        {title:'Hierarchy',     field:'hierarchy',  width:40, align:'right'},
		{title:'Method Name',   field:'methodName', width:160},
		{title:'Used Time(ms)', field:'usedTime',   width:40, align:'right'},
        {title:'Class Name',    field:'className',  width:150}
    ]];

	$('#methodHierarchy').treegrid({
	    columns : methodHierarchyColumns
	});

	//methodHierarchy
	var methodStatisticsColumns = [[
        {title:'packageName',   field:'packageName',   width:300},
        {title:'className',     field:'className',     width:150},
        {title:'methodName',    field:'methodName',    width:280},
        {title:'invokedCount',  field:'invokedCount',  width:100, align:'right'},
        {title:'firstInvoke',   field:'firstInvokeTimeString', width:200},
        {title:'lastInvoke',    field:'lastInvokeTimeString',  width:200},
        {title:'usedTime(ms)',  field:'usedTime',      width:80, align:'right'}
    ]];

	$('#snapshotList').datalist({
	    url: 'listSnapshot',
	    lines: true,
	    columns:[[
	        {field:'description',title:'description',width:100,align:'left'}
	    ]],
	    onClickRow: function(index, snapshot){
	    	//---method Hierarchy
            var methodHierarchyUrl = 'listMethodHierarchy?tracerId=' + snapshot.id;
            console.log( methodHierarchyUrl );
        	$('#methodHierarchy').treegrid({
        	    url       :methodHierarchyUrl,
        	    idField   :'id',
        	    treeField :'methodName',
        	    columns   :methodHierarchyColumns,
        	    onBeforeExpand : function(row) {
        			if (row) {
        				$(this).treegrid('options').url = methodHierarchyUrl + '&threadId=' + row.threadId + "&serial=" + row.serial;
        				console.log( $(this).treegrid('options').url );
        			}
        			return true;
        	    }
        	});

	    	//---method Statistics
            var methodStatisticsUrl = 'listStatistics?tracerId='+ snapshot.id;
        	$('#methodStatistics').datagrid({
        	    url       :methodStatisticsUrl,
        	    columns   :methodStatisticsColumns
        	});
	    }
	});

	function openWebSocket(){
		var websocket = null;
		if('WebSocket' in window){  
		    websocket = new WebSocket("ws://" + window.location.host + "/websocket/1");  
		}else{  
		    alert("您的浏览器不支持websocket");  
		}  
		websocket.onerror = function(){  
			console.log("send error！");
		}  
		websocket.onopen = function(){  
			console.log("connection success！");
		}  
		websocket.onmessage = function(event){  
			console.log(event.data);			
			$('#snapshotList').datalist('reload');			
		}  
		websocket.onclose = function(){  
			console.log("closed websocket!");
		}  
		window.onbeforeunload = function(){ 
		    clos();  
		}
		function clos(){  
		    websocket.close(3000,"强制关闭");  
		}  
	};
	
	openWebSocket();
});
