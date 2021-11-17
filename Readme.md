# SimpleTuringLanguage

is simple programming language for implementing 
Turing-Machines  

## Usage
Input the transition commands in a text-file 
and supply it as the first argument 
when running TurningLanguage.main()

## Syntax
The code is sequence of Transition commands 
separated by whitespaces.
each transition starts with the current value on the tape 
(`0` or `1`) followed by the current state id,
terminated with a `:`
After the `:` follows the new state:
- `0` or `1` for the new value in the current cell
- `<` or `>` signaling movement to the left/right
- the next state id or `X` for terminating the program

Example:

```
00:1>1 10:1<1
01:1<0 11:1>X
```

### generic states
If the movement direction and the next state
are independent of the current cell value, 
a state value of `?` can be used to merge the two cases.
If the current state is `?` the next state can be:
- `0` if the new value is 0 in both cases
- `1` if the new value is 1 in both cases
- `=` if the new value is the same as the old value
- `!` if the new value is the opposite of the old value

Example:

```
?0:=>1 
?1:!<2
?2:1>X
```

### Comments
`//` comments out the rest of the line
Example:

```
00:1>1 10:1<1
01:1<0 //this is a comment
11:1>X
```