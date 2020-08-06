A class TwistGame which determine judgemental methods.

boolean isPlacementWellFormed(String piecePlacement) verify whether the string of piece or peg placement is valid, which means the 4 characters
are in the corresponding range, if the placement is well-formed, return true.

boolean isPlacementStringWellFormed(String placement) determine whether a placement string is well-formed, the length of placement can be divisible
by four; each piece or peg placement is well-formed and in correct alphabetical order; 'a' to 'h' and 'i' just cannot appear more than once;
'j', 'k', and 'l' cannot appear more than twice. Once all requirements are satisfied, return true.

boolean isPlacementStringValid(String placement) determine whether a placement string is valid. While every piece placement is well-formed, in board,
if it overlap, must overlap with a same color peg. Once all requirements are satisfied, return true.

Set<String> getViablePiecePlacements(String placement) return a set which contains all possible and valid single piece placements, these placements cannot
appear already, if symmetric placements are valid, only include lowest rotation. While there is no placement can be used, return null.

String[] getSolutions(String placement) determine whether a starting placement has a unique solution and regard it as challenge, otherwise not challenge,
than return all unique solutions as an array.

