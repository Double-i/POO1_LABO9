package chess.engine.pieces;

import chess.PieceType;
import chess.engine.*;

import java.util.ArrayList;
import java.util.List;

/*TODO: factoriser la promotion(3x copier/coller)
 * */
public class Pawn extends Piece {
    private boolean hasMoved = false;
    private int deltaPlayer = getOwner().getSide() == Side.TOP ? 1 : -1;

    public Pawn(Player owner, ChessBoard chessBoard) {
        super(PieceType.PAWN, owner, chessBoard);
    }

    @Override
    public List<Move> getMoves(int x, int y) {

        List<Move> moves = new ArrayList<Move>();
        ChessBoard chessBoard = this.getChessBoard();

        // Avancer 2 coups
        if (!hasMoved) {
            // on vérife que la case est libre en prenant soit de vérifier que aucune piece ne se situe en elle-même
            // et sa case de destination
            if (chessBoard.isCellEmpty(x, y + 2 * deltaPlayer) && chessBoard.isCellEmpty(x, y + deltaPlayer)) {
                moves.add(new Move(x, y + 2 * deltaPlayer, SpecialMove.PAWN_FAST_MOVE));
            }
        }

        // avance un coup
        if (Move.inBound(x, y + deltaPlayer, chessBoard.getDimension()) && chessBoard.isCellEmpty(x, y + deltaPlayer)) {
            if (canBePromoted(y + deltaPlayer)) {
                moves.add(new Move(x, y + deltaPlayer, SpecialMove.PAWN_PROMOTION));

            } else {
                moves.add(new Move(x, y + deltaPlayer));
            }
        }

        // attaque droite
        if (Move.inBound(x + 1, y + deltaPlayer, chessBoard.getDimension()) && !chessBoard.isCellEmpty(x + 1, y + deltaPlayer) && (chessBoard.getCellAt(x + 1, y + deltaPlayer)).getOwner() != getOwner()) {
            if (canBePromoted(y + deltaPlayer)) {
                moves.add(new Move(x + 1, y + deltaPlayer, SpecialMove.PAWN_PROMOTION));

            } else {
                moves.add(new Move(+ 1, y + deltaPlayer));
            }

        }
        // attaque gauche
        if (Move.inBound(x - 1, y + deltaPlayer, chessBoard.getDimension()) && !chessBoard.isCellEmpty(x - 1, y + deltaPlayer) && (chessBoard.getCellAt(x - 1, y + deltaPlayer)).getOwner() != getOwner()) {

            if (canBePromoted(y + deltaPlayer)) {
                moves.add(new Move(x - 1, y + deltaPlayer, SpecialMove.PAWN_PROMOTION));

            } else {
                moves.add(new Move(x - 1, y + deltaPlayer));

            }

        }

        // prise en passant: on regarde si le dernier mouvement correspond à un déplacement de 2 (d'un pion évidemment)
        // si c'est le cas on vérifie que la destination se trouvait à gauche ou à droite de cette piece (this) si c'est
        // le cas on ajoute la diago correspondante
        Move lastMove = chessBoard.getLastMove();
        if (lastMove != null && lastMove.getSpecialMove() == SpecialMove.PAWN_FAST_MOVE) {
            if (lastMove.getToY() == y) {
                // on ajoute la diagonale droite
                if (Move.inBound(x + 1, y, chessBoard.getDimension()) && lastMove.getToX() == x + 1
                        && chessBoard.isCellEmpty(x + 1, y + deltaPlayer)) {

                    moves.add(new Move(x + 1, y + deltaPlayer, SpecialMove.PAWN_EN_PASSANT));
                }
                // diagonale gauche
                else if (Move.inBound(x - 1, y, chessBoard.getDimension()) && lastMove.getToX() == x - 1
                        && chessBoard.isCellEmpty(x - 1, y + deltaPlayer)) {
                    moves.add(new Move(x - 1, y + deltaPlayer, SpecialMove.PAWN_EN_PASSANT));
                }
            }
        }
        return moves;
    }

    public void hasMoved() {
        this.hasMoved = true;
    }

    private boolean canBePromoted(int y) {
        return y  == 0 || y  == this.getChessBoard().getDimension() - 1;
    }
}
