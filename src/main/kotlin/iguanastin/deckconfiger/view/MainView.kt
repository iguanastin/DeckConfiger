package iguanastin.deckconfiger.view

import iguanastin.deckconfiger.app.MyApp
import iguanastin.deckconfiger.app.Styles
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.geometry.Side
import javafx.scene.control.TabPane
import javafx.scene.layout.StackPane
import javafx.stage.FileChooser
import tornadofx.*

class MainView : View("DeckConfiger ${MyApp.version}") {

    companion object {
        val jsonFileFilter = FileChooser.ExtensionFilter("JSON files", "*.json")
    }

    private val myApp = (app as MyApp)

    private val editor = configeditor(myApp) {
        deckConfigProperty.bind(myApp.deckConfigProperty)
    }

    override val root = borderpane {
        top = initMenuBar()

        center = tabpane {
            side = Side.BOTTOM
            tabDragPolicy = TabPane.TabDragPolicy.FIXED
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
                onAction = EventHandler { event ->
                    event.consume()
                    myApp.syncFromDevice()
                }
            }
            button("Import from file") {
                onAction = EventHandler { event ->
                    event.consume()
                    importFileDialog()
                }
            }
            button("New blank config") {
                onAction = EventHandler { event ->
                    event.consume()
                    myApp.createNewConfig()
                }
            }
        }
    }

    private fun initMenuBar() = menubar {
        menu("File") {
            item("New blank config") {
                onAction = EventHandler { event ->
                    event.consume()
                    myApp.createNewConfig()
                }
            }
            item("Sync to Device") {
                enableWhen(myApp.deckConfigProperty.isNotNull)
                onAction = EventHandler { event ->
                    event.consume()
                    if (!myApp.syncToDevice()) information(
                        "Failed to sync",
                        "Failed to sync to device",
                        owner = currentWindow
                    )
                }
            }
            item("Sync from Device") {
                onAction = EventHandler { event ->
                    event.consume()
                    myApp.syncFromDevice()
                }
            }
            item("Export to File") {
                enableWhen(myApp.deckConfigProperty.isNotNull)
                onAction = EventHandler { event ->
                    event.consume()
                    exportFileDialog()
                }
            }
            item("Import from File") {
                onAction = EventHandler { event ->
                    event.consume()
                    importFileDialog()
                }
            }
            item("Close") {
                enableWhen(myApp.deckConfigProperty.isNotNull)
                onAction = EventHandler { event ->
                    event.consume()
                    myApp.deckConfig = null
                }
            }
            separator()
            item("Exit") {
                onAction = EventHandler { event ->
                    event.consume()
                    Platform.exit()
                }
            }
        }
        menu("Edit") {
            checkmenuitem("Edit Hardware") {
                selectedProperty().bindBidirectional(editor.editHardwareProperty)
            }
            separator()
            item("Add Button") {
                enableWhen(editor.editHardwareProperty)
                onAction = EventHandler { event ->
                    event.consume()
                    editor.openNewButtonDialog()
                }
            }
            item("Add Encoder") {
                enableWhen(editor.editHardwareProperty)
                onAction = EventHandler { event ->
                    event.consume()
                    editor.openNewEncoderDialog()
                }
            }
            item("Add LED") {
                enableWhen(editor.editHardwareProperty)
                onAction = EventHandler { event ->
                    event.consume()
                    editor.openNewLEDDialog()
                }
            }
        }
    }

    init {
        myApp.currentFileProperty.addListener { _, _, newFile ->
            title = "DeckConfiger ${MyApp.version}"
            if (newFile != null) title += " - $newFile"
        }
    }

    private fun importFileDialog() {
        val files = chooseFile(
            "Import Config",
            arrayOf(jsonFileFilter),
            initialDirectory = myApp.currentFile?.parentFile, owner = currentWindow
        )
        if (files.isEmpty()) return

        myApp.loadConfigFromFile(files[0])
    }

    private fun exportFileDialog() {
        val files = chooseFile(
            "Export Config",
            arrayOf(jsonFileFilter),
            initialDirectory = myApp.currentFile?.parentFile, owner = currentWindow, mode = FileChooserMode.Save
        )
        if (files.isEmpty()) return

        myApp.saveConfigToFile(files[0])
    }

}