import {useAuth} from "../context/AuthContext.jsx";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
import {Flex, Heading, Image, Link, Stack, Text} from "@chakra-ui/react";
import CreateCustomerForm from "../shared/CreateCustomerForm.jsx";


const Signup = () => {
    const { customer, setCustomerFromToken } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if (customer){
            navigate("/dashboard")
        }
    })


    return (
        <Stack minH={'100vh'} direction={{ base: 'column', md: 'row' }}>
            <Flex p={8} flex={1} align={'center'} justifyContent={'center'}>
                <Stack spacing={4} w={'full'} maxW={'md'}>
                    <Image
                        src={"https://hamsterkombat.org/images/hamster-coin.png"} boxSize={"100px"} alt={"Hamster Logo"}  />
                    <Heading fontSize={'2xl'} mb={15}>Register your account</Heading>
                    <CreateCustomerForm onSuccess={(token) => {
                        localStorage.setItem("access_token", token);
                        setCustomerFromToken()
                        navigate("/dashboard")
                    }} />
                    <Link color={"blue.500"} href={"/"}>
                        Have an account? Log in!
                    </Link>

                </Stack>
            </Flex>
            <Flex
                flex={1}
                p={10}
                flexDirection={"column"}
                alignItems={"center"}
                justifyContent={"center"} bgGradient={{sm: 'linear(to-r, blue.600, purple.600)'}}>
                <Text fontSize={"6xl"} color={'white'} fontWeight={"bold"} mb={5}>
                    <Link href={"#"}>
                        Contact with CEO
                    </Link>
                </Text>
                <Image
                    alt={'Login Image'}
                    objectFit={'scale-down'}
                    src={
                        'https://img.ixbt.site/live/topics/preview/00/07/03/74/156e0060cb.jpg'
                    }
                />
            </Flex>
        </Stack>
    );
}


export default Signup;