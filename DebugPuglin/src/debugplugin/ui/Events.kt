package debugplugin.ui

/**
 * Created by dhai on 2017-07-10.
 */
import norswap.autumn.model.STNode
import tornadofx.Controller
import tornadofx.EventBus.RunOn.*
import tornadofx.FXEvent

object SyntaxTreeRequest : FXEvent(BackgroundThread)

class SyntaxTreeEvent(val syntax_tree: List<STNode>) : FXEvent()

object STNodeRequest : FXEvent(BackgroundThread)

class STNodeEvent(val node: STNode) : FXEvent()

class UndoEvent(val node: STNode) : FXEvent()

object RedoEvent : FXEvent()

object NoSuchRule : FXEvent()

class RuleRequest(val rule: String) : FXEvent()

class RuleEvent(val code: String) : FXEvent()

class MatchedInputEvent(val input: String) : FXEvent()

class MatchedInputRequest(val node: STNode) : FXEvent()

class SetFilterEvent(val filter: Boolean) : FXEvent()

object ResetFilter : FXEvent()

object RemoveNotMatched : FXEvent()

object BreakpointEvent : FXEvent()