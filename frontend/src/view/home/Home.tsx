import { useEffect, useState } from "react";
import { Outlet } from "react-router-dom";
import Navbar from "../../components/Navbar";
import { UseAuthentication } from "../../contexts/authenticationContext";

function Home() {
  const { fetchdataAuth, setBalance } = UseAuthentication();
  const [showNotifications, setShowNotifications] = useState(false);
  const [notifications, setNotifications] = useState<string[]>([]);

  useEffect(() => {
    fetchdataAuth(`http://localhost:8080/getBalance`, "GET").then((data) => {
      if (typeof data === "number") {
        setBalance(data);
      } else {
        setBalance(0.0);
      }
    });
  }, [fetchdataAuth, setBalance]);

  const changeShow = async () => {
    const newShow = !showNotifications;

    if (newShow) await fetchNotifications();

    setShowNotifications(newShow);
  };

  const fetchNotifications = async () => {
    const data: string[] = await fetchdataAuth(
      "http://localhost:8080/getNotifications",
      "GET",
      undefined,
      { lastNNotifications: 10 }
    );

    if (data) {
      setNotifications(data);
    }
  };

  return (
    <div>
      <Navbar showNotCallback={changeShow} />
      <Outlet />
      {showNotifications && (
        <div className='fixed top-[70px] right-10 w-[400px] h-[500px] bg-gray-100 rounded-2xl border-gray-500 border-2 '>
          <div className='w-full mt-3 text-xl font-semibold text-center'>
            Notifications
          </div>
          <div className='w-full h-[1px] bg-slate-500 mt-3' />
          <div className='h-[90%] w-full overflow-y-auto'>
            <ul>
              {notifications.map((n) => (
                <li key={n}>
                  <p className='p-2'>{n}</p>
                  <div className='h-[1px] w-full bg-gray-300' />
                </li>
              ))}
            </ul>
          </div>
        </div>
      )}
    </div>
  );
}

export default Home;
