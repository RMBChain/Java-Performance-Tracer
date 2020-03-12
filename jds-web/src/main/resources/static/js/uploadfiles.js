$(document).ready(function(){
    $("#files").change(function(){
        //console.log('change::' + document.getElementById('file').files.length );
        var fileListUl = $("#fileListUl");
        var files = document.getElementById('files').files;
        fileListUl.empty();
        for(var i=0; i < files.length; i++ ){
            fileListUl.append("<li>" + files[i].name + "</li>");
        }
        for(var ii in document.getElementById('file').files){
            $("#fileListUl").append("<li>" + ii.name + "</li>");
            console.log('change:' + ii );
        }
    }); 
    
    let date = new Date();
    var dateTxt = "Upload at  " + date.getFullYear() + (date.getMonth() + 1) +  date.getDate() + "_" + date.getHours() + date.getMinutes() + date.getSeconds();
    $("#description").val( dateTxt );
});

function loadFile(){
    $("#uploadFiles").form('submit', {
        type:"post", 
        url:"doUploadFiles",
        success:function(data){
            console.log(data);
            var data =eval('(' + data + ')');
            console.log(data);
            $.messager.alert('Message','Uploaded and parsed ' + data.fileCount + ' files(s)','info',function(){
            	location.href="/";
            });
            return false;
        }
    });  
}
