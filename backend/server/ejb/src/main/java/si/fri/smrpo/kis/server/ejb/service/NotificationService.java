package si.fri.smrpo.kis.server.ejb.service;

import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.models.EmailNotification;
import si.fri.smrpo.kis.server.ejb.service.interfaces.NotificationServiceLocal;
import si.fri.smrpo.kis.server.jpa.entities.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@PermitAll
@Stateless
@Local(NotificationServiceLocal.class)
public class NotificationService implements NotificationServiceLocal {

    private static final Logger LOG = Logger.getLogger(NotificationService.class.getName());

    private static String emailTemplate;


    @Resource(name = "java:jboss/mail/smrpo7")
    private Session session;

    @EJB
    private DatabaseServiceLocal database;

    @PostConstruct
    private void init() {

        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(
                    NotificationService.class.getResourceAsStream("/email-template.html"), "UTF-8"));

            StringBuilder sb = new StringBuilder();
            String line = r.readLine();
            while (line != null) {
                sb.append(line);
                line = r.readLine();
            }

            emailTemplate = sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendEmailNotifications() {
        List<EmailNotification> emailNotifications = buildEmailNotification();

        for(EmailNotification en : emailNotifications) {

            String title = en.getReceiver().getFirstName() + " " + en.getReceiver().getLastName();
            String email = en.getReceiver().getEmail();

            String tableContent = en.getTableContent();

            String emailContent = emailTemplate
                    .replace("$USERNAME$", title)
                    .replace("$CONTENT$", tableContent);


            sendEmail(email,"Card due date notification" ,emailContent);
        }
    }

    private void sendEmail(String email, String subject, String content) {

        try {

            Message message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(subject);
            message.setContent(content, "text/html; charset=utf-8");

            Transport.send(message);

        } catch (MessagingException e) {
            LOG.log(Level.WARNING, "Failed send mail", e);
        }
    }

    private List<EmailNotification> buildEmailNotification() {

        List<DevTeam> allDevTeams = database.stream(DevTeam.class).toList();

        HashMap<UUID, EmailNotification> kanbanMasterMap = new HashMap<>();

        for(DevTeam dt : allDevTeams) {

            UserAccount km = dt.getKanbanMaster();

            EmailNotification en = kanbanMasterMap.get(km.getId());
            if(en == null) {
                en = new EmailNotification(km);
                kanbanMasterMap.put(km.getId(), en);
            }


            for(Project p : dt.getProjects()) {
                Board b = p.getBoard();

                if(b.getRemainingDays() != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DAY_OF_MONTH, b.getRemainingDays());
                    Date warning = cal.getTime();

                    for(Card c : p.getCards()) {
                        if(c.getDueDate() != null) {
                            if(c.getDueDate().before(warning)) {
                                if(c.getBoardPart().getLeafNumber() <= b.getAcceptanceTesting()) {
                                    en.addCard(c);
                                }
                            }
                        }
                    }
                }
            }
        }

        List<EmailNotification> emails = new ArrayList<>();
        for(EmailNotification en : kanbanMasterMap.values()) {
            if(en.getCards().size() > 0) {
                emails.add(en);
            }
        }

        return emails;
    }



}
