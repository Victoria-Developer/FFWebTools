function setCharacterInfoVisible(buttonSource){
       var panel = buttonSource.next('.characterInfoPanel');
       var isHidden = JSON.parse(buttonSource.val());
       buttonSource.text(isHidden ? 'Show' : 'Hide');
       buttonSource.val(!isHidden);
       panel.toggle();
    }