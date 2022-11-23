import { UseAuthentication } from "../../contexts/authenticationContext";

function Home() {
    const auth = UseAuthentication();
    return (


        <button onClick={auth.logout}>logout</button>
    )
}

export default Home;