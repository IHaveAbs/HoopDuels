# HoopDuels

HoopDuels is a basketball strategy game I built in Java using Swing. You coach an NBA team against the computer. You call the plays, you pick the defense, and the game animates every possession on a court until the clock runs out.

Made for AP Computer Science A.

## Screenshots

Picking an offensive play. Your team is on the left, the computer's on the right, and the four plays sit on the panel.

![Choosing an offensive play](screenshots/choose_offensive.png)

Now on defense. Same court, but the buttons switch to the four defensive schemes, and the notes under the play log remind you what each one does to the shooting odds.

![Choosing a defensive strategy](screenshots/choose_defense.png)

## What it is

You manage one team, one possession at a time, against a computer opponent. Offense means picking a play. Defense means picking a scheme that changes how likely the computer is to score. Once you choose, the five players move, pass, and shoot, and the shot either drops or it doesn't. Whoever has more points when time expires wins. A tie sends you to overtime.

## How to play

1. Type your name on the first screen.
2. Pick your team: Warriors, Lakers, Celtics, or Thunder.
3. Choose your starting five from the roster.
4. Play the game. You and the computer trade possessions.
   - On offense you get four plays:
     - **Pick and Roll** sets a screen up top, then the big rolls to the rim.
     - **Isolation Drive** clears everyone out so your wing can attack.
     - **Kick-Out Three** drives into the paint and kicks the ball back out for three.
     - **Post Up** feeds your center on the block.
   - On defense you pick a scheme, and it shifts the computer's shooting odds:
     - **Man-to-Man** does nothing special.
     - **Zone Defense** locks down the rim but leaves the three open.
     - **Protect Paint** takes away the rim even harder, so threes get easier for them.
     - **Guard Perimeter** chases shooters off the line and gives up drives.
5. Whether a shot goes in depends on the shooter's rating for that kind of shot, your defense, and a little luck. Makes are worth 2 or 3. Misses get rebounded, and the defense grabs about 70% of them.
6. The clock is 12 game minutes, sped up so a match doesn't take forever. When it hits zero the higher score wins. If it's tied, overtime is sudden death. Next basket ends it.
7. When the game's over you get a box score with everyone's points, rebounds, assists, and shooting.

One thing worth knowing about the computer opponent. It runs the same scripted plays to open, then goes random after that. But if it's losing late, under three minutes, it starts hunting threes to climb back.

## Running it

You need a JDK. I wrote this on Java 17.

```bash
# from the project root
javac *.java
java HoopDuels
```

## Techniques used

- **Swing with `CardLayout`.** The whole thing is one window that swaps between screens: name, menu, team select, starting five, game, post-game. `HoopDuels` handles the swapping.
- **A real game loop.** `GamePanel` runs a `javax.swing.Timer` at roughly 60 frames per second. Every tick moves things a little and repaints, which is what makes the plays actually animate instead of snapping into place.
- **A state machine.** The game tracks what it's doing with states like `waitingforuser`, `useranimating`, and `aitransition`. It only moves to the next step once every player has reached their spot and the ball has stopped. Figuring this part out took the longest.
- **Splitting the logic into managers.** Instead of cramming everything into one class, I split it up. `PlayManager` handles play movement, `DefenseManager` handles the shot modifiers, `ShotCalculator` decides makes and misses, and `PlayerManager` holds the rosters. The computer opponent lives in its own manager class too.
- **A `Drawable` interface.** `Player` and `Ball` both implement it, so anything on the court can be drawn the same way.
- **Data structures.** Each player's shooting stats sit in a `HashMap`. The computer's opening plays sit in a `Queue`. Play positions are hard-coded 2D arrays that get mirrored when a team attacks the other basket.

## What's next

There are two directions I could take this. One is a two-player mode, so two people could play each other on the same keyboard instead of always facing the computer. The other, and the one I'd rather build, is making the computer opponent run on real AI instead of the scripted plays it uses now. That one is on hold until I learn how to actually work with AI.
