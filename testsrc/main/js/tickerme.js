(function($){
$.fn.tickerme = function(options) {
var opts = $.extend( {}, $.fn.tickerme.defaults, options );
return this.each(function(){
var ticker = $(this);
// SVG definitions for the play/pause/previous/next controls:
var control_definitions = '<svg display="none" version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="224" height="32" viewBox="0 0 224 32"><defs><g id="icon-play"><path class="path1" d="M6 4l20 12-20 12z"></path></g><g id="icon-pause"><path class="path1" d="M4 4h10v24h-10zM18 4h10v24h-10z"></path></g><g id="icon-prev"><path class="path1" d="M18 5v10l10-10v22l-10-10v10l-11-11z"></path></g><g id="icon-next"><path class="path1" d="M16 27v-10l-10 10v-22l10 10v-10l11 11z"></path></g></defs></svg>';
var control_styles = '<style type="text/css">#ticker_container{width:100%}#newscontent{float:left}#news{display:none}#controls{float:right;height:16px}.icon{display:inline-block;width:16px;height:16px;fill:'+opts.control_colour+'}.icon:hover{fill:'+opts.control_rollover+'}</style>';
// Array to contain news contents:
var contents = [];
var position = -1;
var timer;
init();
/* Initialise */
function init() {
// Hide all:
$(ticker).hide();
// Create the buttons:
$('body').prepend(control_definitions).prepend(control_styles);
var controls = '<div id="ticker_container"><div class="news-sec">In the news</div>';
controls += '<div id="newscontent"><div id="news"></div></div>';
controls += '<div id="controls">'; 
controls += '<a href="#" id="prev_trigger"></a><div class="more"><a href="news.html">MORE NEWS</a></div><br>';
controls += '<a href="#" id="next_trigger"></a>';
controls += '</div>';
controls += '</div>';
$(controls).insertAfter(ticker);
// Load up the array:
$(ticker).children().each(function(i){
contents[i] = ($(this).html());
});
load_container();
}
/* load_container */
function load_container() {
if (position == (contents.length - 1)) {
position = 0;
} else {
position++;
}		
// Fade out the current item, replace it with the next one, and fade it in:
if (opts.type == 'fade') {
$('#news').fadeOut(opts.fade_speed,function(){
$('#newscontent').html('<div id="news">'+contents[position]+'</div>');
$('#news').fadeIn(opts.fade_speed);
});
}
timer = setTimeout(load_container,opts.duration);
}
/* Control functions */
$('a#pause_trigger').click(function() {
clearTimeout(timer);
$(this).hide();
$('#play_trigger').show();
return false;
});
$('a#play_trigger').click(function(){
load_container();
$(this).hide();
$('#pause_trigger').show();
return false;
});
$('a#prev_trigger').click(function(){
if (position == 0) {
position = (contents.length - 1);
} else {
position--;
}
$('#newscontent').html('<div id="news" style="display:block">'+contents[position]+'</div>');
if (opts.auto_stop) $('a#pause_trigger').trigger('click');
return false;
});
$('a#next_trigger').click(function(){
if (position == (contents.length - 1)) {
position = 0;
} else {
position++;
}
$('#newscontent').html('<div id="news" style="display:block">'+contents[position]+'</div>');
if (opts.auto_stop) $('a#pause_trigger').trigger('click');
return false;
});
});
};
$.fn.tickerme.defaults = {
fade_speed: 500,
duration: 3000,
auto_stop: true,
type: 'fade',
control_colour: '#333333',
control_rollover: '#666666'
};
}(jQuery));
// tabbed content
$(".tab_content").hide();
$(".tab_content:first").show();
/* if in tab mode */
$("ul.tabs li").click(function() {
$(".tab_content").hide();
var activeTab = $(this).attr("rel"); 
$("#"+activeTab).fadeIn();		
$("ul.tabs li").removeClass("active");
$(this).addClass("active");
$(".tab_drawer_heading").removeClass("d_active");
$(".tab_drawer_heading[rel^='"+activeTab+"']").addClass("d_active");
});
/* if in drawer mode */
$(".tab_drawer_heading").click(function() {
var d_activeTab = $(this).attr("rel"); 
if($("#"+d_activeTab).is(':visible')){
$("#"+d_activeTab).fadeOut();
$(this).removeClass("d_active");
}else{
$(".tab_content").hide();
$("#"+d_activeTab).fadeIn();
$(".tab_drawer_heading").removeClass("d_active");
$(this).addClass("d_active");
$("ul.tabs li").removeClass("active");
$("ul.tabs li[rel^='"+d_activeTab+"']").addClass("active");
}
});
/* Extra class "tab_last" 
to add border to right side
of last tab */
$('ul.tabs li').last().addClass("tab_last");