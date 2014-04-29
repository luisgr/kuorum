<form id="search-form" class="navbar-form navbar-left" role="search">
    <div class="input-group">
        <div class="loadingSearch"><span class="sr-only">Cargando...</span></div>
        <div class="input-group-btn">
            <button class="btn search" type="submit"><span class="fa fa-search"></span></button>
        </div>
        <input type="text" name="word" class="form-control" placeholder="En todo kuorum.org" id="srch-term"/>
        <input type="hidden" name="type" id="srch-type" value="KUORUM_USER"/>
        <a data-target="#" href="/dashboard.htm" class="dropdown-toggle" id="open-filter-search" data-toggle="dropdown" role="button"><span class="sr-only">Filtra tu búsqueda</span> <span class="fa fa-caret-down fa-lg"></span></a>
        <ul id="filters" class="dropdown-menu dropdown-menu-right" aria-labelledby="open-filter-search" role="menu">
            <li><a href="#" id="filtro01">En todo kuorum</a></li>
            <li><a href="#" id="filtro02">En Leyes</a></li>
            <li><a href="#" id="filtro03">En Personas</a></li>
        </ul>
    </div>
</form>
<script>
    function getFileterType(){
        return $("#srch-type").val()
    }
    $(function(){
        var a = $('#srch-term').autocomplete({
            paramName:"word",
            params:{type:getFileterType},
            serviceUrl:urls.searchSuggest,
            minChars:1,
            width:330,
            noCache: false, //default is false, set to true to disable caching
            onSearchStart: function (query) {
                $('.loadingSearch').show()
            },
            onSearchComplete: function (query, suggestions) {
                $('.loadingSearch').hide()
            },
            formatResult:function (suggestion, currentValue) {
                var format = ""
                if (suggestion.type=="SUGGESTION"){
                    format =  suggestion.value
                }else if(suggestion.type=="USER"){
                    format = "<img class='user-img' alt='"+suggestion.data.name+"' src='"+suggestion.data.urlAvatar+"'>"
                    format +="<span class='name'>"+suggestion.data.name+"</span>"
                    format +="<span class='user-type'>"+suggestion.data.role.i18n+"</span>"
                }else if(suggestion.type=="LAW"){
                    format = "<span class='statusLaw'>"+suggestion.data.status.i18n+"</span>"
                    format += suggestion.data.title
                    format += " <strong>"+suggestion.data.hashtag+"</strong>"
                }
                return format
            },
            searchUserText:function(userText){
                window.location = urls.search+"?word="+userText
            },
            onSelect: function(suggestion){
                if(suggestion.type=="USER"){
                    window.location = suggestion.data.url
                }else if(suggestion.type=="LAW"){
                    window.location = suggestion.data.url
                }else{
                    window.location = urls.search+"?word="+userText
                }
            },
            triggerSelectOnValidInput:false,
            deferRequestBy: 100 //miliseconds
        });
    });

</script>