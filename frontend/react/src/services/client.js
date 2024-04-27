import axios from "axios";

export const getCustomers = async () => {

    try{
        return await axios.get(`http://localhost:8080/api/v1/customers`)
    }catch(e){
        throw e;
    }

}

export const saveCustomer = async (registrationRequest) => {
    try{
        return await axios.post(`http://localhost:8080/api/v1/customers`, registrationRequest)
    }catch(e){
        throw e;
    }
}


export const updateCustomer = async (id, update) => {
    try{
        return await axios.put(`http://localhost:8080/api/v1/customers/${id}`, update)
    }catch(e){
        throw e;
    }
}

export const deleteCustomer = async (id) => {
    try{
        return await axios.delete(`http://localhost:8080/api/v1/customers/${id}`)
    }catch(e){
        throw e;
    }
}