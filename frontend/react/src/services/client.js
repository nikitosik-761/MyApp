import axios from "axios";


const getAuthConfig = () => ({
    headers: {
        Authorization: `Bearer ${localStorage.getItem("access_token")}`
    }
})


export const getCustomers = async () => {

    try{
        return await axios.get(
            `http://localhost:8080/api/v1/customers`,
            getAuthConfig()
        )
    }catch(e){
        throw e;
    }

}

export const saveCustomer = async (registrationRequest) => {
    try{
        return await axios.post(
            `http://localhost:8080/api/v1/customers`,
            registrationRequest
        )
    }catch(e){
        throw e;
    }
}


export const updateCustomer = async (id, update) => {
    try{
        return await axios.put(
            `http://localhost:8080/api/v1/customers/${id}`,
            update,
            getAuthConfig())
    }catch(e){
        throw e;
    }
}

export const deleteCustomer = async (id) => {
    try{
        return await axios.delete(`http://localhost:8080/api/v1/customers/${id}`, getAuthConfig())
    }catch(e){
        throw e;
    }
}

export const login = async (usernameAndPassword) => {
    try{
        return await axios.post(`http://localhost:8080/api/v1/auth/login`, usernameAndPassword)
    }catch(e){
        throw e;
    }
}