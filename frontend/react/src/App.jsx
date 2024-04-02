import UserProfile from "./UserProfile.jsx"


function App() {
  return (
    <div>
      <UserProfile
      name = {"Nigas"}
      age = {21}
      gender = {"women"}>

      <p>Hello</p>

      </UserProfile>
        <UserProfile
            name = {"Piska"}
            age = {24}
            gender = {"men"}>

        </UserProfile>
    </div>
  );
}

export default App
