import axios from 'axios';
import React, { createContext, useReducer } from 'react';
import Reducer from './Reducer'

const initialState = {
    products : undefined,
    markets : undefined,
    loggedIn : false,
    userinfo : {}
}

 export const instance = axios.create({
    baseURL: 'http://127.0.0.1:8080/',
    timeout: 1000
})
export const GlobalProvider = ({ children }) => {
    const [state, dispatch] = useReducer(Reducer, initialState);
 
    return (
        <Context.Provider value={[state, dispatch]}>
            {children}
        </Context.Provider>
    )
 }
 export const Context = createContext(initialState);