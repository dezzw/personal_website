import { BsGithub, BsLinkedin, BsCloudDownload } from 'react-icons/bs';
import { IoIosMail } from 'react-icons/io';

function Contact() {
  return (
    <div className="contact card title">
      <div className="">Get in touch</div>
      <div className="my-2 lg:my-8 font-body text-sm lg:text-xl text-gray-500">
        To contact me further, feel free to check out my personal profiles
        below:
      </div>
      <div className="my-2 lg:my-8 flex items-center text-3xl lg:text-5xl">
        <a href="url">
          <IoIosMail className="lg:mr-4 text-5xl lg:text-7xl hover:text-gray" />
        </a>
        <a href="https://github.com/dezzw">
          <BsGithub className="mx-2 lg:mx-4 hover:text-gray" />
        </a>
        <a href="url">
          <BsLinkedin className="mx-2 lg:mx-4 text-[#0b66c2] hover:text-gray" />
        </a>
        <a href="">
          <div className="mx-2 lg:mx-4 flex items-center justify-center rounded bg-prime-2 p-1 lg:p-2 text-2xl lg:text-4xl text-white hover:bg-gray">
            <BsCloudDownload className="mr-1 lg:mr-2" />{' '}
            <span className="font-body text-sm lg:text-2xl">Resume</span>
          </div>
        </a>
      </div>
    </div>
  );
}

export default Contact;
