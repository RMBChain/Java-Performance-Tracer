import axios from "axios";

axios.defaults.baseURL = "/api"

axios.interceptors.response.use(
    res => res.data,
    err => Promise.reject(err)
)

export const listHosts = ()=> axios.get("/metric/findHost")

export const findTracers = ( params )=> axios.get("/metric/findTracers",{
    params : params
})

export const listMetrics = ( params )=> axios.get("/metric/listMetrics",{
    params : params
})

export const getStatistics = ( params )=> axios.get("/metric/statistics",{
    params
})

export const getAnalysisRange = (  )=> axios.get("/analysisRange/getAnalysisRange")

export const saveAnalysisRange = ( params )=> axios.post("/analysisRange/saveAnalysisRange",
    params
)

export const deleteTracer = ( params )=> axios.get("/management/deleteTracer", {
    params
});

export const saveTracer = ( params )=> axios.post("/management/saveTracer",
    params
);

export const findLogs = ( params )=> axios.get("/management/findLogs", {
    params
});
