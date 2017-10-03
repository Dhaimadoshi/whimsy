package debugplugin.ui

/**
 * Created by dhai on 2017-07-10.
 */
import norswap.autumn.model.STNode
import tornadofx.Controller
import tornadofx.EventBus.RunOn.*
import tornadofx.FXEvent

object SyntaxTableRequest : FXEvent(BackgroundThread)
object SyntaxTreeRequest : FXEvent()

class SyntaxTableEvent(val syntax_tree: List<STNode>) : FXEvent()
class SyntaxTreeEvent(val node: STNode) : FXEvent()

class RuleRequest(val rule: String) : FXEvent()
class RuleEvent(val code: String) : FXEvent()

class MatchedInputRequest(val node: STNode) : FXEvent()
class MatchedInputEvent(val input: String) : FXEvent()

class SetFilterEvent(val filter: Boolean) : FXEvent()
object ResetFilter : FXEvent()

object RemoveNotMatched : FXEvent()
object RemoveBacktracked : FXEvent()

class SetRootRequest(val selectedItem: STNode?) : FXEvent()
class AddRootRequest(val items: ArrayList<STNode>) :FXEvent()