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
                item("Apply") {
                    enableWhen(myApp.deckConfigProperty.isNotNull)
                    onAction = EventHandler { event ->
                        event.consume()
                        // TODO
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
            scrollpane(fitToWidth = true, fitToHeight = true) {
                isPannable = true

                content = pane {
                    myApp.deckConfigProperty.addListener { _ ->
                        children.clear()
                        // TODO
                    }
                }
            }

            vbox(10.0) {
                hiddenWhen(myApp.deckConfigProperty.isNotNull)
                alignment = Pos.CENTER
                label("Nothing open, yet") {
                    addClass(Styles.noConfigWarning)
                }
                hyperlink("Import") {
                    onAction = EventHandler { event ->
                        event.consume()
                        importFileDialog()
                    }
                }
            }

            anchorpane {
                isPickOnBounds = false
                button("Apply") {
                    anchorpaneConstraints {
                        rightAnchor = 25
                        bottomAnchor = 25
                    }
                    enableWhen(myApp.unsavedChangesProperty)
                    onAction = EventHandler { event ->
                        event.consume()
                        // TODO
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