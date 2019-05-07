var new_window=null;
function reloadTOC(pURL)
{
  if((new_window != null) &&(!new_window.closed))
  {
    new_window.close();
  }
  new_window = open(pURL,'','resizable=1,scrollbars=yes,menubar=1,toolbar=1,status=1,outerHeight='+screen.availHeight+',outerWidth=720'+screen.availWidth+'');
}

