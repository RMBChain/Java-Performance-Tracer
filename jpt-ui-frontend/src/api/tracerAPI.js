import axios from "axios";

axios.defaults.baseURL = "/"

axios.interceptors.response.use(
    res => res.data,
    err => Promise.reject(err)
)

export const listHosts = ()=> axios.get("/listHost")

export const listTracers = ( params )=> axios.get("/listTracers",{
    params : params
})

export const listMetrics = ( params )=> axios.get("/listMetrics",{
    params : params
})

export const getInjectConfig = (  )=> axios.get("/getInjectConfig")

export const saveInjectConfig = ( params )=> axios.post("/saveInjectConfig",
    params
)

export const clearAllData = ( params )=> axios.post("/clearAllData")

export const getStatistics = ( params )=> axios.get("/statistics",{
    params
})


