object WaterPouringTypes {
  type Jug = Int
  type State = List[Int]
}
import WaterPouringTypes._

class WaterPouring(val JUGS: List[Jug] = List(3, 4), val GOAL: Integer = 2) {
  val INITIAL_STATE: State = JUGS.map(_=>0) //All jugs are empty to begin with
  val JUG_INDEX_RANGE = 0 to JUGS.length - 1 toList //Index iterator over all jugs

  trait Move {
    def doMove(state: State): State
  }
  case class Empty(jugIndex: Jug) extends Move {
    override def doMove(state: State): State = state updated (jugIndex, 0)
  }
  case class Fill(jugIndex: Jug) extends Move {
    override def doMove(state: State): State = state updated (jugIndex, JUGS(jugIndex))
  }
  case class Pour(fromJugIndex: Jug, toJugIndex: Jug) extends Move {
    override def doMove(state: State): State = {
      val pourAmt = math.min(state(fromJugIndex), JUGS(toJugIndex) - state(toJugIndex))
      state updated (fromJugIndex, state(fromJugIndex) - pourAmt) updated (toJugIndex, state(toJugIndex) + pourAmt)
    }
  }

  class Path(val moves: List[Move], val endState: State) {
    override def toString: String = moves + " yielding " + endState
  }

  val EMPTY_MOVES = for (jug <- JUG_INDEX_RANGE) yield Empty(jug)
  val FILL_MOVES = for(jug <- JUG_INDEX_RANGE) yield Fill(jug)
  val POUR_MOVES = for {
    fromJug <- JUG_INDEX_RANGE
    toJug <- JUG_INDEX_RANGE
    if fromJug != toJug
  } yield Pour(fromJug, toJug)
  val MOVES: List[Move] = EMPTY_MOVES ++ FILL_MOVES ++ POUR_MOVES

  def createPaths(path: Path): Stream[(Move, State)] =
    MOVES.toStream.map(m => (m, m.doMove(path.endState)))

  def solve(paths: Stream[Path], history: Set[State]): Stream[Path] =
    paths match {
      case Stream.Empty => Stream()
      case head #:: xs => {
        val next = createPaths(head)
          .filter(t => !(history contains(t._2)))
          .map(t => new Path(head.moves :+ t._1, t._2))
        head #:: solve(xs #::: next, history | next.map(t=>t.endState).toSet)
      }
    }

  def findSolution() =
    solve(Stream(new Path(List(), INITIAL_STATE)), Set(INITIAL_STATE))
      .find(p => p.endState.contains(GOAL))
}

object Solver extends App {
  var w = new WaterPouring(List(3, 5), 4)
  val solution = w.findSolution()
  if(solution.isDefined)
    println("Yipeekayay! Found a solution at " + solution.get)
  else println("No solution found, John")

}
