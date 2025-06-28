package iguanastin.deckconfiger.view

import iguanastin.deckconfiger.app.MyApp
import iguanastin.deckconfiger.app.Styles
import javafx.application.Platform
import javafx.geometry.Pos
import javafx.geometry.Side
import javafx.scene.control.TabPane.TabDragPolicy
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.layout.StackPane
import javafx.stage.FileChooser
import tornadofx.*

class MainView : View("DeckConfiger ${MyApp.VERSION}") {

    companion object {
        private val JSON_FILE_FILTER = FileChooser.ExtensionFilter("JSON files", "*.json")
    }

    private val myApp = (app as MyApp)

    private val editor = configeditor(myApp)

    override val root = topenabledstackpane {
        borderpane {
            this.top = initMenuBar()

            this.center = tabpane {
                this.side = Side.BOTTOM
                this.tabDragPolicy = TabDragPolicy.FIXED
                tab("Config") {
                    closableProperty().set(false)
                    stackpane {
                        add(editor)

                        initEditorSetupButtons()

                        initEditorOverlay()
                    }
                }
                tab("Console") {
                    closableProperty().set(false)
                    serialconsole(myApp.serial)
                }
            }
        }
    }


    init {
        myApp.currentFileProperty.addListener { _, _, newFile ->
            title = "DeckConfiger ${MyApp.VERSION}"
            if (newFile != null) title += " - $newFile"
        }
    }


    private fun StackPane.initEditorOverlay() {
        anchorpane {
            isPickOnBounds = false
            val connLabel = label {
                prefWidthProperty().bind(heightProperty())
                alignment = Pos.CENTER
                anchorpaneConstraints {
                    bottomAnchor = 10
                    rightAnchor = 10
                }
                val update = { new: Boolean ->
                    if (new) {
                        text = "✔"
                        addClass(Styles.textGreen)
                        removeClass(Styles.textRed)
                    } else {
                        text = "X"
                        addClass(Styles.textRed)
                        removeClass(Styles.textGreen)
                    }
                }
                addClass(Styles.connectedIcon)
                update(myApp.serial.connected)
                myApp.serial.connectedProperty.addListener { _, _, new ->
                    runOnUIThread {
                        update(new)
                    }
                }
            }
            label {
                anchorpaneConstraints {
                    bottomAnchor = 10
                    rightAnchor = 50
                }
                visibleWhen(connLabel.hoverProperty().or(myApp.serial.connectedProperty.not()))
                addClass(Styles.connectedIcon)
                text = "Not connected to deck"
                myApp.serial.connectedProperty.addListener { _, _, new ->
                    runOnUIThread {
                        text = if (new) "Connected to deck" else "Not connected to deck"
                    }
                }
            }
        }
    }

    private fun StackPane.initEditorSetupButtons() {
        vbox(10) {
            isPickOnBounds = false
            alignment = Pos.CENTER
            hiddenWhen(myApp.deckConfigProperty.isNotNull)
            button("Sync from device") {
                enableWhen(myApp.serial.connectedProperty)
                onActionConsuming {
                    myApp.syncFromDevice()
                }
            }
            button("Import from file") {
                onActionConsuming {
                    importFileDialog()
                }
            }
            button("New blank config") {
                onActionConsuming {
                    myApp.createNewConfig()
                }
            }
        }
    }

    private fun initMenuBar() = menubar {
        menu("File") {
            item("New blank config") {
                accelerator = KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN)
                onActionConsuming { myApp.createNewConfig() }
            }
            item("Sync to Device") {
                accelerator = KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN)
                enableWhen(myApp.deckConfigProperty.isNotNull)
                onActionConsuming {
                    if (!myApp.syncToDevice()) information(
                        "Failed to sync",
                        "Failed to sync to device",
                        owner = currentWindow
                    )
                }
            }
            item("Sync from Device") {
                accelerator = KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN)
                onActionConsuming { myApp.syncFromDevice() }
            }
            item("Export to File") {
                accelerator = KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN)
                enableWhen(myApp.deckConfigProperty.isNotNull)
                onActionConsuming { exportFileDialog() }
            }
            item("Import from File") {
                accelerator = KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN)
                onActionConsuming { importFileDialog() }
            }
            item("Close Config") {
                accelerator = KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN)
                enableWhen(myApp.deckConfigProperty.isNotNull)
                onActionConsuming { myApp.deckConfig = null }
            }
            separator()
            item("Close Editor") {
                accelerator = KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN)
                onActionConsuming { currentStage?.hide() }
            }
            item("Exit") {
                accelerator = KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN)
                onActionConsuming { Platform.exit() }
            }
        }
        menu("Edit") {
            checkmenuitem("Edit Hardware") {
                selectedProperty().bindBidirectional(editor.editHardwareProperty)
            }
            separator()
            item("Add Button") {
                enableWhen(editor.editHardwareProperty)
                onActionConsuming { editor.openNewButtonDialog() }
            }
            item("Add Encoder") {
                enableWhen(editor.editHardwareProperty)
                onActionConsuming { editor.openNewEncoderDialog() }
            }
            item("Add LED") {
                enableWhen(editor.editHardwareProperty)
                onActionConsuming { editor.openNewLEDDialog() }
            }
            item("Add RGB") {
                enableWhen(editor.editHardwareProperty)
                onActionConsuming { editor.openNewRGBDialog() }
            }
        }
    }

    private fun importFileDialog() {
        val files = chooseFile(
            "Import Config",
            arrayOf(JSON_FILE_FILTER),
            initialDirectory = myApp.currentFile?.parentFile, owner = currentWindow
        )
        if (files.isEmpty()) return

        myApp.loadConfigFromFile(files[0])
    }

    private fun exportFileDialog() {
        val files = chooseFile(
            "Export Config",
            arrayOf(JSON_FILE_FILTER),
            initialDirectory = myApp.currentFile?.parentFile, owner = currentWindow, mode = FileChooserMode.Save
        )
        if (files.isEmpty()) return

        myApp.saveConfigToFile(files[0])
    }

}