var isTooltipEnabled = false;
$(".tooltipCheckBox").click(function(e){
isTooltipEnabled = !isTooltipEnabled;
revalidateCanvas();
});