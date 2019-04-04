class Mail:

    def add(self,data) :
        file = open("report.txt",'a')
        file.write(data + '\n')
        file.close()

    def send_report_to_admin(self,subject):
        import smtplib
        from email.mime.multipart import MIMEMultipart
        from email.mime.text import MIMEText

        msg = MIMEMultipart()
        msg['From'] = 'vkubuntuserver@gmail.com'
        msg['To'] = 'vkubuntuserver@gmail.com'
        msg['Subject'] = subject
        message = ""

        file = open("report.txt",'r').readlines()
        for line in file:
            message = message + line

        msg.attach(MIMEText(message))
        mailserver = smtplib.SMTP('smtp.gmail.com', 587)
        mailserver.ehlo()
        mailserver.starttls()
        mailserver.ehlo()
        mailserver.login('vkubuntuserver@gmail.com', "eCNHxqZeApw5")
        mailserver.sendmail('vkubuntuserver@gmail.com', 'vkubuntuserver@gmail.com', msg.as_string())
        mailserver.quit()
        file = open("report.txt",'w')
