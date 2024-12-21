Thread: 
A thread describes in which context sequences of instructions should be executed. 

Advantages of Co-routine: 
can do all the things that a thread can do plus some advantages 
we can start many co-routine inside a single thread

1. Executed within a thread 
2. are suspendable (we can pause the execution in the middle and continue when need)
3. they can switch their context (can switch from one thread to another)
4. No limitation of co-routine creation

we can called it as light-weight thread with extra features 

**Global Scope**
1. lives as long as our app lives 
2. launch another thread other than main thread

``delay -> only delay statement pause only current co-routine, not blocking whole thread (vs Sleep in thread)``

if main thread finishes, all thread in coroutine cancelled automatically!! 

suspend func only call from coroutine scope or other suspend function

**Coroutine Context**
context says which thread it is started
we need to pass `dispatcher` to control context or thread 
- Dispatcher.Main -> access to main thread (can update UI things)
- Dispatcher.IO -> for network call, db queries and ops
- Dispatcher.Default -> long running task like sorting an array of 1 million items, same as IO
- Dispatcher.Unconfined -> not confined to any particular thread

We can also pass custom thread to handle coroutine ops like: 
``GlobalScope.launch(newSingleThreadContext("threadName)) {}``

- How to switch context inside same coroutine scope:

``
GlobalScope.launch(Dispatcher.IO) {
    val result = fromNetworkCall()
    withContext(Dispatcher.Main) { // switching context here
        //access UI elements to show result
    }
}
``
**runBlocking**
block the main thread like Thread.Sleep func 
run blocking provide us coroutine scope which enables us to write suspend func, also launch new 
coroutine scope (by launch(Dispatcher){}) inside run-blocking scope





