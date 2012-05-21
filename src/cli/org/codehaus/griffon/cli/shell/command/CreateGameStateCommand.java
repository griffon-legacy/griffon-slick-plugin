package org.codehaus.griffon.cli.shell.command;

import org.codehaus.griffon.cli.shell.AbstractGriffonCommand;
import org.codehaus.griffon.cli.shell.Command;
import org.codehaus.griffon.cli.shell.Argument;
import org.codehaus.griffon.cli.shell.Option;

@Command(scope = "slick",
        name = "create-game-state",
        description = "Creates a new GameState")
public class CreateGameStateCommand extends AbstractGriffonCommand {
    @Argument(index = 0,
            name = "name",
            description = "The name of the game state to be created.",
            required = false)
    private String name;

    @Option(name = "--skip-package-prompt",
            description = "Skips the usage of the application's default package if the name of the class is not fully qualified.",
            required = false)
    private boolean skipPackagePrompt = false;

    @Option(name = "--file-type",
            description = "Source file type.",
            required = false)
    private String fileType = "groovy";

    @Option(name = "--archetype",
            description = "Archetype to be searched for templates.",
            required = false)
    private String archetype = "default";

    @Option(name = "--with-model",
            description = "Fully qualified className Model to use.\nWARNING: the command will not create a file for this member.",
            required = false)
    private String withModel;

    @Option(name = "--with-controller",
            description = "Fully qualified className Controller to use.\nWARNING: the command will not create a file for this member.",
            required = false)
    private String withController;

    @Option(name = "--skip-model",
            description = "Skips the creation of the model MVC member.",
            required = false)
    private String skipModel;

    @Option(name = "--skip-controller",
            description = "Skips the creation of the controller MVC member.",
            required = false)
    private String skipController;

    @Option(name = "--model",
            description = "Specifies the Model template to be used.",
            required = false)
    private String model = "Model";

    @Option(name = "--state",
            description = "Specifies the GameState template to be used.",
            required = false)
    private String state = "GameState";

    @Option(name = "--controller",
            description = "Specifies the Controller template to be used.",
            required = false)
    private String controller = "Controller";

    @Option(name = "--group",
            description = "Specifies the common template to use on all MVC members.",
            required = false)
    private String group;
}
