import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)
const actions = {
    setUsertype(context,value){
        context.commit('SETUSERTYPE', value)

    },
    setUsername(context,value){
        context.commit('SETUSERNAME',value)
    },
    setUserid(context,value){
        context.commit('SETUSERID',value)
    },
    setToken(context,value){
        context.commit('SETTOKEN',value)
    }
}

const mutations = {
    SETUSERTYPE(state,value){
        state.usertype = value
    },
    SETUSERNAME(state,value){
        state.username = value
    },
    SETUSERID(state,value){
        state.userid = value
    },
    SETTOKEN(state,value){
        state.token = value
    }
}

const state = {
    usertype:"user",
    username:"",
    userid:"",
    token:""
}

export default new Vuex.Store({
    actions,
    mutations,
    state

})
