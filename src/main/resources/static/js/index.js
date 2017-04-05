isHide = {};
dataMap = {};
function keyword(key) {
    var needHide = true;
    if(isHide[key] == true) {
        needHide = false;
        isHide[key] = false;
    } else {
        isHide[key] = true;
    }

    $('p').each(function(){
        if($(this).html().indexOf(key) == -1) {
            if(needHide) {
                $(this).attr(key+'_hide', '1');
                $(this).hide();
            } else {
                if($(this).attr(key+'_hide') == '1') {
                    $(this).show();
                    $(this).removeAttr(key+'_hide');
                }
            }
        }
    });
}

function showAll() {
    $('p').each(function(){
        $(this).show();
    });
}

function priceBetween(min, max) {
    for(var i in dataMap){
        var flag = true;
        /*console.dir(i);//输出姓名
        console.dir(dataMap[i]);//输出分数*/
        if(min != '') {
            if(dataMap[i]['price'] < min) {
                flag = false;
            }
        }
        if(max != '') {
            if(dataMap[i]['price'] > max) {
                flag = false;
            }
        }
        if(!flag) {
            //不满足条件
            $('#'+i).hide();
        }

    }
}

function showTime(timestamp) {

    var time = new Date(parseInt(timestamp)*1000);
    var y = time.getFullYear();//年
    var m = time.getMonth() + 1;//月
    var d = time.getDate();//日
    var h = time.getHours();//时
    var mm = time.getMinutes();//分
    var s = time.getSeconds();//秒
    return ''+y+' '+m+ ' '+d+' '+h+':'+mm;
}
name = '9kuai9';
function setName(key) {
    name = key;
    $('#article_name').text(key);
}
function loadData(url){
    $('p').remove();
    $.post(name + url, function(data){
        console.log(data);
        data.forEach(function(val,index,arr){
            dataMap[val['article_id']] = val;
            var p = $('<p>');
            p.attr('id', val['article_id']);
            p.html(val['article_date']+' | '+val['article_mall']+' | '+val['article_price']+' | '+val['article_title']
                + ' | <a target="_blank" href="'+ val['article_url'] + '">'+val['article_url']+'</a>');
            $('body').append(p);
        });
    });
}
$(function(){
    $('body').append('<button onclick="setName(this.innerHTML)">9kuai9</button><button onclick="setName(this.innerHTML)">jingxuan</button>');
    $('body').append('<button onclick="loadData(\'/price/asc\')">价格</button>');
    $('body').append('<button onclick="loadData(\'/time/desc\')">时间</button>');
    $('body').append('<button onclick="loadData(\'/comment/desc\')">评论</button>');
    $('body').append('<button onclick="loadData(\'/worthy/desc\')">值得</button>');
    $('body').append('<button onclick="showAll()">显示所有</button>');
    $('body').append('<button onclick="keyword(this.innerHTML)">包邮</button>');
    $('body').append('<button onclick="keyword(this.innerHTML)">天猫</button>');
    $('body').append('<button onclick="keyword(this.innerHTML)">京东</button>');
    $('body').append('<button onclick="priceBetween(-1, 3)">3元以内</button>');
    $('body').append('<button onclick="priceBetween(-1, 5)">5元以内</button>');
    $('body').append('<button onclick="priceBetween(-1, 7)">7元以内</button>');
    $('body').append('<button onclick="priceBetween(-1, 10)">10元以内</button>');
    $('body').append('<input id="search" /> <button onclick="keyword(document.getElementById(\'search\').value)">确定</button>');
    $('body').append('<h2 id="article_name">9kuai9</h2>');
    $.post('/9kuai9/price/asc', function(data){
        console.log(data);
        data.forEach(function(val,index,arr){
            dataMap[val['article_id']] = val;
            var p = $('<p>');
            p.attr('id', val['article_id']);
            p.html(val['article_date']+' | '+val['article_mall']+' | '+val['article_price']+' | '+val['article_title']
                + ' | <a target="_blank" href="'+ val['article_url'] + '">'+val['article_url']+'</a>');
            $('body').append(p);
        });
    });
});