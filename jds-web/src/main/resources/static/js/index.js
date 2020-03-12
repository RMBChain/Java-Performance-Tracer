$(document).ready(function(){
	//method Hierarchy
	var methodHierarchyColumns = [[
        {title:'Thread',    field:'threadId', width:80, align:'right'},
        {title:'Serial',    field:'serial',   width:80, align:'right'},
        {title:'Hierarchy', field:'hierarchy',width:80, align:'right'},	        
        {title:'Qualified Class Name',  field:'qualifiedClassName',  width:260, hidden:'true'},
        {title:'Qualified Method Name', field:'qualifiedMethodName', width:260, hidden:'true'},
        {title:'Class And Method Name', field:'classAndMethodName',  width:700},
        {title:'Class Name',            field:'className',           width:260, hidden:'true'},
        {title:'Method Name',           field:'methodName',          width:80,  hidden:'true'},
        {title:'Used Time(ms)',             field:'usedTime',            width:80,  align:'right'},
        {title:'Package Name',          field:'packageName',         width:500},
    ]];

	$('#methodHierarchy').treegrid({
	    columns : methodHierarchyColumns,
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
	    onClickRow: function(index, row){
	    	//method Hierarchy
            var methodHierarchyUrl = 'listMethodHierarchy?snapshotId=' + row.id + '&parentId=';
            console.log( methodHierarchyUrl );

        	$('#methodHierarchy').treegrid({
        	    url       :methodHierarchyUrl,
        	    idField   :'id',
        	    treeField :'classAndMethodName',
        	    columns   :methodHierarchyColumns,
        	    onBeforeExpand : function(row) {
        			if (row) {				
        				$(this).treegrid('options').url = methodHierarchyUrl + row.id;
        				console.log( $(this).treegrid('options').url );
        			}
        			return true;
        	    }
        	});

	    	//method Statistics
            var methodStatisticsUrl = 'listStatistics?snapshotId='+ row.id;
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
