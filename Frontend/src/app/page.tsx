import Home from '../pages/Home';
import UserVerification from '../components/UserVerification';

const HomePage = () => {
  return (
    <UserVerification>
      <Home />
    </UserVerification>
  );
};

export default HomePage;
