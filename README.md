## about
advent is a text adventure engine that specializes with choose your own adventure style games.
 
## dta
advent uses a custom format known as dta(data text application). dta is fairly simple.

### example
```dta
~a reagion, this is like a json object (sort of); btw this is a comment
#example
    key:value

    #inner_region
        other:key value pair
        this_can_only_be_one_word: this is always seen as a string
    #end
#end

.GLOBALCONSTANT = some value that can't be changed
```

### syntax hilighting
there is a vscode extension in one of these folders

<!-- hi, this is not finished yet. :( -->

<!-- ## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies). -->
