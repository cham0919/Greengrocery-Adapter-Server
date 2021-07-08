window.onload = fetchGreengroceryList();

var list;
var GreengroceryInfo = {
    FRUIT : {value : "FRUIT", uri : "/greengrocery/fruit"},
    VEGETABLE : {value : "VEGETABLE", uri : "/greengrocery/vegetable"}
};

function fetchGreengroceryList(){
    $.ajax({
        type : "GET",
        url : "/greengrocery",
        dataType : "json",
        success : function(data){
            let fruitDOM = document.getElementById( 'fruit' );
            let vegetableDOM = document.getElementById( 'vegetable' );
            list = data;
            updateGreengroceryList(list["FRUIT"])
        },
        error : function(data){
            alert(data);
        }
    });
}


function updateGreengroceryList(greengroceryList) {
    let dom = document.getElementById( 'list' );
    let innerText = [];
    for (let i = 0; i < greengroceryList.length; i++){
        innerText.push(greengroceryList[i].name);
    }
    dom.innerText = "목록 : [ " + innerText + " ]";
}

function visible(v) {
    updateGreengroceryList(list[v])
}

function fetchPrice(){
    let name = document.getElementById( 'name' ).value;
    let kindDOM = document.getElementById( 'kind' );
    let kind = kindDOM.options[kindDOM.selectedIndex].value
    let price = document.getElementById( 'price' );
    let warning = document.getElementById( 'warning' );

    let data = { name : name }
    if (name.trim() === "") {
        warning.innerText = "이름을 적어주세요";
        price.innerText = "";
        return
    } else if (!isInclude(name, kind)){
        warning.innerText = "목록에 없습니다. 다시 한번 확인해주세요";
        price.innerText = "";
        return
    } else {
        warning.innerText = "";
    }

    $.ajax({
            type : "GET",
            url : GreengroceryInfo[kind].uri,
            data : { name : name },
            dataType : "json",
            success : function(data){
                price.innerText = data["price"] + "원"
            },
            error : function(data){
                alert(data);
            }
        });
}

function isInclude(name, kind){
    for (let i = 0; i < list[kind].length; i++){
        if (list[kind][i].name == name) return true
    }
    return false;
}