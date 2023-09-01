package cz.lastaapps.languagetool.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import arrow.fx.coroutines.resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


open class StateViewModel<State : VMState>(initial: State) : ViewModel() {
    private val state = MutableStateFlow(initial)

    protected fun updateState(function: State.() -> State) =
        state.update(function)

    protected fun latestState() = state.value

    val flow = state.asStateFlow()
    val flowState
        @Composable
        get() = state.collectAsStateWithLifecycle()

    private var didAppear: Boolean = false
    private val didAppearMutex = Mutex()
    protected fun launchOnlyOnce(function: suspend () -> Unit): Unit = launchVM {
        didAppearMutex.withLock {
            if (didAppear) {
                return@launchVM
            } else {
                didAppear = true
                function()
            }
        }
    }

    protected suspend fun <R> withLoading(
        loading: State.(isLoading: Boolean) -> State,
        block: suspend () -> R,
    ) = resource(
        acquire = { updateState { loading(true) } },
        release = { _, _ -> updateState { loading(false) } },
    ).use { block() }
}

fun ViewModel.launchVMJob(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) = viewModelScope.launch(context, start, block)

fun ViewModel.launchVM(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) = launchVMJob(context, start, block).let { }

context(ViewModel)
fun <T> Flow<T>.launchInVM() = this.launchIn(viewModelScope)

@Immutable
interface VMState
