import axios from "axios";

export const getCustomers = async () => {

    try{
        return await axios.get(`http://localhost:8080/api/v1/customers`)
    }catch(e){
        throw e;
    }
    //

}

export const saveCustomer = async (registrationRequest) => {
    try{
        return await axios.post(`http://localhost:8080/api/v1/customers`, registrationRequest)
    }catch(e){
        throw e;
    }
}