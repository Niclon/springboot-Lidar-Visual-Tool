class CustomGuiItemNameSelection {
    constructor() {
        this._prepareGuiForSelection();
        this._initActions();
    }

    _initActions() {
        let that = this;
        document.addEventListener("keydown", keyDownBody, false);

        function keyDownBody(e) {
            var keyCode = that._getNumberFromKeyEvent(e);
            if (keyCode != null) {
                console.log(keyCode);
                let length = that.selectedItemNamesList.length;
                if (keyCode < length) {
                    if ($('#itemNameModal').is(":visible")) {
                        $('#selected-item-name-input')[0].value = that.selectedItemNamesList[keyCode].itemName;
                        $('#selected-item-name-input-submit').click();
                    }
                }
            }
        }
    }

    showModalWithWaitingForActions() {
        return new Promise(function (resolve, reject) {
            $('#myModal').off();
            $('#myModal').on('hidden.bs.modal', function (e) {
                resolve(null);
            });
            $('#selected-item-name-input')[0].value = '';
            $('#selected-item-name-input-submit').off();
            $('#selected-item-name-input-submit').on('click', function (e) {
                let inputValue = $('#selected-item-name-input')[0].value;
                if (inputValue && inputValue.length === 0) {
                    $('#itemNameModal').modal('hide');
                    resolve(null);
                }
                resolve(inputValue);
                $('#itemNameModal').modal('hide');
            });

            $('#itemNameModal').modal('show')
        })

    }

    async prepareData() {
        return this.getAllSelectedItemNames();

    }

    getAllSelectedItemNames() {
        return new Promise(function (resolve, reject) {
            const http = new XMLHttpRequest();
            http.open('GET', '/selectedItem/get/all/names', false);
            http.setRequestHeader('Content-type', 'application/json');
            http.onreadystatechange = function () {
                if (http.readyState === 4 && http.status === 200) {
                    console.log(http.responseText);
                    resolve(JSON.parse(http.response));
                }
            };
            http.send(); // Make sure to stringify
        });
    }

    async _prepareGuiForSelection() {
        let customItemNameContainer = document.querySelector('#my-gui-selected-item-name-container');
        this.selectedItemNamesList = await this.prepareData();
        let itemNamesLength = this.selectedItemNamesList.length;
        for (let i = 0; i < itemNamesLength; i++) {
            let name = this.selectedItemNamesList[i].itemName;
            if (i <= 9) {
                name = i + '. ' + name;
            }
            let item = document.createElement("a");
            item.innerHTML = name;
            item.className = "w3-bar-item w3-button ItemNameSelectorClass";
            item.setAttribute('itemIndex', i);
            customItemNameContainer.appendChild(item);
        }
        this._initActionClickableActions()
    }

    _initActionClickableActions() {
        let selectedItemNames = document.getElementsByClassName('ItemNameSelectorClass');
        let that = this;
        for (let selectedItemName of selectedItemNames) {
            selectedItemName.onclick = function () {
                // console.log(selectedItemName.getAttribute('itemIndex'));
                // console.log(that.selectedItemNamesList[selectedItemName.getAttribute('itemIndex')])
                if ($('#itemNameModal').is(":visible")) {
                    $('#selected-item-name-input')[0].value = that.selectedItemNamesList[selectedItemName.getAttribute('itemIndex')].itemName;
                    $('#selected-item-name-input-submit').click();
                }
            }
        }
    }

    addNewItemNameToSelection(newItemNameFromDB) {
        let alreadyContained = this.selectedItemNamesList.filter(item => (item.id === newItemNameFromDB.id));
        if (alreadyContained){
            return;
        }

        let customItemNameContainer = document.querySelector('#my-gui-selected-item-name-container');
        let itemNamesLength = this.selectedItemNamesList.length;

        let that = this;
        let name = newItemNameFromDB.itemName;
        let newItemIndex = itemNamesLength + 1;
        if (itemNamesLength <= 9) {
            name = newItemIndex + '. ' + name;
        }
        this.selectedItemNamesList.push(newItemNameFromDB);

        let item = document.createElement("a");
        item.innerHTML = name;
        item.className = "w3-bar-item w3-button ItemNameSelectorClass";
        item.setAttribute('itemIndex', newItemIndex);
        item.onclick = function () {
            if ($('#itemNameModal').is(":visible")) {
                $('#selected-item-name-input')[0].value = that.selectedItemNamesList[item.getAttribute('itemIndex')].itemName;
                $('#selected-item-name-input-submit').click();
            }
        };
        customItemNameContainer.appendChild(item);

    }

    _getNumberFromKeyEvent(event) {
        if (event.keyCode >= 96 && event.keyCode <= 105) {
            return event.keyCode - 96;
        } else if (event.keyCode >= 48 && event.keyCode <= 57) {
            return event.keyCode - 48;
        }
        return null;
    }
}

export default CustomGuiItemNameSelection