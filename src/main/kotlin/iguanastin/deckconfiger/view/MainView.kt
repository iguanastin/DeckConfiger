package iguanastin.deckconfiger.view

import iguanastin.deckconfiger.app.MyApp
import iguanastin.deckconfiger.app.config.DeckConfig
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.geometry.Pos
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
        top = menubar {
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
                        myApp.syncToDevice()
                    }
                }
                item("Sync from Device") {
                    enableWhen(myApp.deckConfigProperty.isNotNull)
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
            }
        }

        center = stackpane {
            add(editor)

            vbox(10) {
                alignment = Pos.CENTER
                hiddenWhen(myApp.deckConfigProperty.isNotNull)
                hyperlink("Import from file") {
                    onAction = EventHandler { event ->
                        event.consume()
                        importFileDialog()
                    }
                }
                hyperlink("New blank config") {
                    onAction = EventHandler { event ->
                        event.consume()
                        myApp.createNewConfig()
                    }
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