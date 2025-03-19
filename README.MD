Update: Please see my new project <https://github.com/Slavrick/Godot5DChess> Which is built in Godot, with a better and more friendly UI, and more portable (hopefully)

# Purpose

The purpose of this project is to create an open GUI for 5dchess engines. The GUI will be a functional game(for instance it could be used to play a local player vs player game of 5d chess), however it is still recommended to use the steam implementation of the game for competitive, and casual play as it is much more polished, and will support the developer who made the ruleset of 5d chess. 

The main Purpose or features of this specific GUI will be to 1. allow users to perform some game analysis and 2. allow eventually for engines to interface with the GUI for matches and evaluation of positions.

## Features

* GUI For 5d Chess
* All Rules and pieces of the base game implemented
* Ability to load games through PGN/FEN files
* Ability to save games to a PGN/FEN file(only in raw notation)
* Analysis tools such as sidelines, rewinding and jumping to positions(this is present in other implementations such as chessin5d but not in the base game)
* White/Black only views

## Proposed Features

*  Study System(Loading, creating saving)
*  Ability to interface with 5d Engines in a UCI like protocol
*  Human V. Bot and Bot V. Bot Matches.
*  Variant Creator menu

# Screen Shots

![Main-GUI-upon-opening](https://i.imgur.com/RLRPs4T.png)

![Ex-2](https://i.imgur.com/0QvgLBx.png)


# Notation

This main notation that this project uses is Shad Amethyst's notation, which is the unofficial standard notation for 5d Chess. Using this Notation, games can be loaded and will be exportable in this notation in the near future. You can read more about this notation here: <https://github.com/adri326/5dchess-notation/blob/master/README.md>

## Other Notations

### PGN5 Notation

Some print statements use the notation that looks like:

> [w.3T0L.a1]

This is similar to my old notation, and is only for debugging purposes, as I have no way to parse this kind of notation at the moment.

### Coordinate Notation

This GUI also can parse a custom raw notation, dissimilar to other notations developed by other 5dChess community members. This notation is not meant to be human readable, but rather meant for an engine. I have dubbed this notation __**Coordinate Notation**__.

In a 2d chess engine, you may notice that moves are denoted in long algebraic notation _ie_.

 > e2e4

In this gui, notation for a move will look like
>(x<sub>origin</sub>,y<sub>origin</sub>,T<sub>origin</sub>,L<sub>origin</sub>)(x<sub>dest</sub>,y<sub>dest</sub>,T<sub>dest</sub>,L<sub>dest</sub>)

With x being the file, y being rank, T being time, and L being Layer/Timeline.
A whole Turn will look like

>(x<sub>M1o</sub>,y<sub>M1o</sub>,T<sub>M1o</sub>,L<sub>M1o</sub>)(x<sub>M1d</sub>,y<sub>M1d</sub>,T<sub>M1d</sub>,L<sub>M1d</sub>);(x<sub>M2o</sub>,y<sub>M2o</sub>,T<sub>M2o</sub>,L<sub>M2o</sub>)(x<sub>M2d</sub>,y<sub>M2d</sub>,T<sub>M2d</sub>,L<sub>M2d</sub>)…

With moves separated by semicolons. A promotion would look like

> (x<sub>origin</sub>,y<sub>origin</sub>,T<sub>origin</sub>,L<sub>origin</sub>)(x<sub>dest</sub>,y<sub>dest</sub>,T<sub>dest</sub>,L<sub>dest</sub>)=Q

and castling is denoted normally, with layer added

> &lt;MOVE&gt;;O-O,L;&lt;MOVE&gt;

Lastly, there may be the need to show a null move, which is denoted as:

> 0000

Formally, the moves should follow an order where destination layer is from top to bottom however this is not enforced by the engine. Branching moves must always be ordered how they occur however.

# UCI

This project will eventually implement a protocol to interface with engines. I am now considering UCI and looking at the referece <https://www.stmintz.com/ccc/index.php?id=360181>.

The UCI will either use raw notation of shads moves

> (0T1)e2(0T1)e4

or coordinate notation

# FEN

This project currently uses Shad Amethyst's Standard listed above to load games. It is in a somewhat buggy state, so if there is something wrong with the PGN/FEN the program will fail silently without telling you what was wrong.

## Old FEN notation

This project uses a custom FEN format to load gamestates into the GUI/ into the engines. In the next few releases this will be removed as it is deprecated.

The format that is used is based off of the FEN format with some tweaks. The starting position in normal fen is:
 
> rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1

with each field separated by whitespace. Read more about Fen at the wiki page:

<https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation>

You can find out more about the format in this google doc <https://docs.google.com/document/d/11ER6cLA31S-MZYTma7KutqR2AbqPuMjxY5F-dUCMhxQ/edit> -- It is not final however.

In the future I may add functionality to specify mate in x with a puzzle. Puzzles will not be viable however until there is an engine that can make correct defensive/offensive refutations of certain moves. One workaround would be able to specify a refutation table for each possible move, however this may be tedious for puzzle makers.

## Examples

example of this format can be found in the /res/ and /res/variations/ folders.

## Studies

I would like to implement some sort of study system into this GUI, but currently I am not at the stage yet to implement it and also the study notation format is not finalized as far as I know. therefore, some waiting will have to be done in order to figure out what can be done about study systems.

However, the study system once implemented will be able to load PGN files that have commentary and arrows/highlights displayed as well as sidelines which is mainly used for instructional purposes(such as showing an opening etc.)

# Other Projects and resources

**The Best Resource for 5d Chess Programming is AlexBay's Discord Channel: <https://discord.gg/KP5vApW>**

Original 5d Implementation on steam: <https://5dchesswithmultiversetimetravel.com/>

There are several other open source projects that are also being worked on:

Alexbay's GitLab:<https://gitlab.com/alexbay218/5d-chess-js>

Alexbay's 5dChess:<https://chessin5d.net/#/>

Shad's Engine:<https://github.com/adri326/5dchess-tools/>

L0laapk3 5dChess website: <https://multiversechess.com/>

# Notes

This project is being developed using eclipse, all source code is in the src folder. I will periodically export the code as a jar file. This project uses Javafx, which has caused compatibility issues in the past. If you cannot my program, please create an issue and provide relevant information.

## Copyright

This engine was made without reference to source or decompiled code from the software "5d Chess With Multiverse Time Travel" developed by LLC. Thunkspace. All code, assets, libraries and other files in this project were either made by me, or are open source.

The project itself is open source.














