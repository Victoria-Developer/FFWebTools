function fillTable(jsonList){
        var tableBody = $('#tb');
        tableBody.empty();
        for (object in jsonList) {
            var character = jsonList[object];
            var newRow = tableBody[0].insertRow();
            var newCell = newRow.insertCell();

            div = document.createElement("div");
            characterInfoButton = document.createElement("button");
            characterInfoButton.innerHTML = 'Show';
            characterInfoButton.className = "characterInfoButton";
            characterInfoButton.value = false;
            characterInfoButton.onclick = function(){
            setCharacterInfoVisible($(this))
            };
            var innerDiv = document.createElement("div");
            innerDiv.className = "characterInfoPanel";

            innerDiv.appendChild(fillCharacterInfo(character));

            minions = document.createElement("p");
            minions.innerHTML = "Minions number: " + character.minionsNumber;
            for (minion in character.minionsList){
               minions.innerHTML += ", " + character.minionsList[minion]
            }
            innerDiv.appendChild(minions);

            mounts = document.createElement("p");
            mounts.innerHTML = "Mounts number: " + character.mountsNumber;
            for (mount in character.mountsList){
               mounts.innerHTML += ", " + character.mountsList[mount]
            }
            innerDiv.appendChild(mounts);

            a = document.createElement("a");
            a.setAttribute("href", character.characterLink);
            a.text = character.characterName;

            div.appendChild(a);
            div.appendChild(characterInfoButton);
            div.appendChild(innerDiv);
            newCell.appendChild(div);
            }
        }

    function fillCharacterInfo(character){
        p = document.createElement("p");
        p1 = document.createElement("p");
        p1.innerHTML = "Capped jobs amount: " + character.cappedJobsNumber;
        table = document.createElement("table");
        table.className = "characterInfoTable";
        var tableBody = document.createElement("tbody");
        table.appendChild(tableBody);
        p.appendChild(p1);
        p.appendChild(table);

        for(j in character.jobs){
        var newRow = tableBody.insertRow();
        var jobNameCell = newRow.insertCell();
        var jobLvlCell = newRow.insertCell();
        jobNameCell.innerHTML = j;
        jobLvlCell.innerHTML = character.jobs[j];
        }
        return p;
    }