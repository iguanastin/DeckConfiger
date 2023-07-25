package iguanastin.deckconfiger.view

import iguanastin.deckconfiger.app.MyApp
import iguanastin.deckconfiger.app.Styles
import iguanastin.deckconfiger.app.config.DeckConfig
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import javafx.stage.FileChooser
import tornadofx.*

class MainView : View("DeckConfiger ${MyApp.version}") {

    companion object {
        val jsonFileFilter = FileChooser.ExtensionFilter("JSON files", "*.json")
    }

    private val myApp = (app as MyApp)

    override val root = borderpane {
        top = menubar {
            menu("File") {
                item("New empty config") {
                    onAction = EventHandler { event ->
                        event.consume()
                        myApp.createNewConfig()
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
                separator()
                item("Exit") {
                    onAction = EventHandler { event ->
                        event.consume()
                        Platform.exit()
                    }
                }
            }
        }

        center = stackpane {
            configeditor {
                deckConfigProperty.bind(myApp.deckConfigProperty)
            }

            hyperlink("Import") {
                hiddenWhen(myApp.deckConfigProperty.isNotNull)
                onAction = EventHandler { event ->
                    event.consume()
                    importFileDialog()
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