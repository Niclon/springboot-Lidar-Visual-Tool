class CustomDatGuiMenu {
    constructor(lidarPointsInstance, maxStepNumber) {
        this.lidarPointsInstance = lidarPointsInstance;
        this.maxStepNumber = maxStepNumber;
    }

    createDatGuiSliderAndActionMenu() {
        const dat = require('dat.gui');
        const gui = new dat.GUI({autoPlace: false});
        const guiAction = new dat.GUI({autoPlace: false});
        let lidarPointsInstance = this.lidarPointsInstance;
        let that = this;
        let simulateKeyDown = function (keycode, isCtrl, isAlt, isShift) {
            var e = new KeyboardEvent("keydown", {
                bubbles: true,
                cancelable: true,
                char: String.fromCharCode(keycode),
                key: String.fromCharCode(keycode),
                shiftKey: isShift,
                ctrlKey: isCtrl,
                altKey: isAlt
            });
            Object.defineProperty(e, 'keyCode', {
                get: function () {
                    return this.keyCodeVal;
                }
            });
            e.keyCodeVal = keycode;
            document.dispatchEvent(e);
        };
        let updateSlider = function (name, value) {
            for (var i = 0; i < gui.__controllers.length; i++) {
                if (gui.__controllers[i].property === name)
                    gui.__controllers[i].setValue(value);
            }
        };

        var guiFunction = function () {
            this.stepNumber = 0;
            this.startDrawing = function () {
                // ctrl key
                simulateKeyDown(0, true, false, false)

            };
            this.discardDrawingChanges = function () {
                // alt key
                simulateKeyDown(0, false, true, false)

            };
            this.submitDrawing = function () {
                // shift key
                simulateKeyDown(0, false, false, true)
            };
            this.enableMovingOfBorders = function () {
                // M key
                simulateKeyDown(77, false, false, false)
            };
            this.enableResizingOfBorders = function () {
                // N key
                simulateKeyDown(78, false, false, false);
            };
            this.howToDeleteBorder = function () {
                // delete key but create border
                alert("First you need to have enabled moving of borders and then you point to wanted border and click delete key on your keyboard")
            };
            this.nextStep = function () {
                if (this.stepNumber < that.maxStepNumber) {
                    this.stepNumber += 1;
                    updateSlider('stepNumber', this.stepNumber);
                }
            };
            this.previousStep = function () {
                if (this.stepNumber !== 0) {
                    this.stepNumber -= 1;
                    updateSlider('stepNumber', this.stepNumber);
                }
            }
        };
        let variable = new guiFunction();


        let stepNumber = gui.add(variable, 'stepNumber').min(0).max(this.maxStepNumber).step(1);
        gui.add(variable, 'nextStep').name('Next -->');
        gui.add(variable, 'previousStep').name('Previous <--');

        let actionFolder = guiAction.addFolder('Actions');
        actionFolder.add(variable, 'startDrawing').name("Start drawing (Ctrl)");
        actionFolder.add(variable, 'discardDrawingChanges').name('Discard drawing changes (Alt)');
        actionFolder.add(variable, 'submitDrawing').name('Submit drawing (Shift)');
        actionFolder.add(variable, 'enableMovingOfBorders').name('Enable/Disable moving of borders (M)');
        actionFolder.add(variable, 'enableResizingOfBorders').name('Enable/Disable resizing of borders (N)');
        actionFolder.add(variable, 'howToDeleteBorder').name('How to delete borders (Del)');


        stepNumber.onChange(function (value) {
            if (value === lidarPointsInstance.state.stepNumber) {
                return;
            }
            lidarPointsInstance.state.stepNumber = value;
            lidarPointsInstance.render();
        });
        let customContainer = document.querySelector('#my-gui-slider-container');
        if (customContainer != null) customContainer.appendChild(gui.domElement);
        let customActionContainer = document.querySelector('#my-gui-action-container');
        if (customActionContainer != null) customActionContainer.appendChild(guiAction.domElement);

    }
}

export default CustomDatGuiMenu;