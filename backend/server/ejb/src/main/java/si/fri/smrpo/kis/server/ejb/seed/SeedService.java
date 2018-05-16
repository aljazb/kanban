package si.fri.smrpo.kis.server.ejb.seed;

import io.bloco.faker.Faker;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.service.interfaces.CardMoveServiceLocal;
import si.fri.smrpo.kis.server.ejb.service.interfaces.CardServiceLocal;
import si.fri.smrpo.kis.server.jpa.entities.*;
import si.fri.smrpo.kis.server.jpa.entities.Membership;
import si.fri.smrpo.kis.server.jpa.enums.CardType;
import si.fri.smrpo.kis.server.jpa.enums.MemberType;
import si.fri.smrpo.kis.server.jpa.enums.RequestStatus;
import si.fri.smrpo.kis.server.jpa.enums.RequestType;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.*;
import java.util.stream.Collectors;

@Startup
@Singleton
public class SeedService {

    private static final Faker FAKER = new Faker();

    private static final String TEST_USER_ID = "b0ee9324-e4bc-40a3-8068-d4f7042dae60";
    private static final String ADMIN_USER_ID = "29b3a75e-0c4f-4aa7-a505-5290af252abe";
    private static final String DEVELOPER_USER_ID = "c8bca3a6-b49e-45fe-bd14-af82472933e9";
    private static final String KANBAN_MASTER_USER_ID = "944848a6-be17-404e-8ecc-a9247a094702";
    private static final String PRODUCT_OWNER_USER_ID = "37f2b970-63ee-4c36-bafb-2447d3a405ad";

    private UserAccount testAccount;
    private UserAccount adminAccount;
    private UserAccount developerAccount;
    private UserAccount kanbanMasterAccount;
    private UserAccount productOwnerAccount;


    @EJB
    private DatabaseServiceLocal database;

    @EJB
    private CardMoveServiceLocal cardMoveService;

    @EJB
    private CardServiceLocal cardService;


    private DevTeam testDevTeam;
    private Board testBoard;
    private HashMap<Integer, BoardPart> testBoardPartLeafs;

    private Project testProject1;
    private ArrayList<Card> testCards1;

    private Project testProject2;
    private ArrayList<Card> testCards2;


    @PostConstruct
    public void init() {
        if(isDatabaseEmpty()){
            try {
                generateUserAccounts();
                generateDevTeams();
                generateBoard();
                generateBoardParts();
                generateProjects();
                generateCards();
                generateRequests();
                generateCardMoves();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isDatabaseEmpty(){
        List<UserAccount> userAccounts = database.getEntityManager()
                .createNamedQuery("user-account.get-all", UserAccount.class)
                .setMaxResults(1).getResultList();

        return userAccounts.isEmpty();
    }

    private UserAccount genUserAccount(String username) {
        UserAccount ua = new UserAccount();
        ua.setUsername(username);
        ua.setEmail(FAKER.internet.email());
        ua.setFirstName(FAKER.name.firstName());
        ua.setLastName(FAKER.name.lastName());
        ua.setInRoleDeveloper(false);
        ua.setInRoleKanbanMaster(false);
        ua.setInRoleAdministrator(false);
        ua.setInRoleProductOwner(false);
        return ua;
    }

    private void generateUserAccounts() throws DatabaseException {

        testAccount = genUserAccount("test");
        testAccount.setId(UUID.fromString(TEST_USER_ID));
        testAccount.setEmail("test@test.com");
        testAccount.setInRoleKanbanMaster(true);
        testAccount.setInRoleAdministrator(true);
        testAccount.setInRoleProductOwner(true);
        testAccount.setInRoleDeveloper(true);
        testAccount = database.create(testAccount);

        adminAccount = genUserAccount("ta");
        adminAccount.setId(UUID.fromString(ADMIN_USER_ID));
        adminAccount.setEmail("admin@admin.com");
        adminAccount.setInRoleAdministrator(true);
        adminAccount = database.create(adminAccount);

        developerAccount = genUserAccount("td");
        developerAccount.setId(UUID.fromString(DEVELOPER_USER_ID));
        developerAccount.setEmail("developer@developer.com");
        developerAccount.setInRoleDeveloper(true);
        developerAccount = database.create(developerAccount);

        kanbanMasterAccount = genUserAccount("tkm");
        kanbanMasterAccount.setId(UUID.fromString(KANBAN_MASTER_USER_ID));
        kanbanMasterAccount.setEmail("kanban@master.com");
        kanbanMasterAccount.setInRoleKanbanMaster(true);
        kanbanMasterAccount = database.create(kanbanMasterAccount);

        productOwnerAccount = genUserAccount("tpo");
        productOwnerAccount.setId(UUID.fromString(PRODUCT_OWNER_USER_ID));
        productOwnerAccount.setEmail("product@owner.com");
        productOwnerAccount.setInRoleProductOwner(true);
        productOwnerAccount = database.create(productOwnerAccount);
    }

    private void generateDevTeams() throws DatabaseException {

        testDevTeam = new DevTeam();
        testDevTeam.setName(FAKER.company.name());
        testDevTeam = database.create(testDevTeam);

        Membership m = new Membership();
        m.setUserAccount(kanbanMasterAccount);
        m.setDevTeam(testDevTeam);
        m.setMemberType(MemberType.KANBAN_MASTER);
        database.create(m);

        m = new Membership();
        m.setUserAccount(productOwnerAccount);
        m.setDevTeam(testDevTeam);
        m.setMemberType(MemberType.PRODUCT_OWNER);
        database.create(m);

        m = new Membership();
        m.setUserAccount(developerAccount);
        m.setDevTeam(testDevTeam);
        m.setMemberType(MemberType.DEVELOPER);
        database.create(m);

        database.update(testDevTeam);
    }

    private void generateBoard() throws DatabaseException {

        testBoard = new Board();
        testBoard.setOwner(kanbanMasterAccount);
        testBoard.setName("Kanbanize");

        testBoard.setHighestPriority(1);
        testBoard.setStartDev(2);
        testBoard.setEndDev(6);
        testBoard.setAcceptanceTesting(7);

        testBoard = database.create(testBoard);
    }


    private BoardPart createBoardPart(String name, Integer maxWip) {
        BoardPart bp = new BoardPart();
        bp.setCurrentWip(0);
        bp.setBoard(testBoard);
        bp.setMaxWip(maxWip);
        bp.setName(name);
        return bp;
    }

    private void generateBoardParts() throws DatabaseException {
        testBoardPartLeafs = new HashMap<>();

        BoardPart bp = createBoardPart("Product backlog", 0);
        bp.setOrderIndex(0);
        bp.setLeafNumber(0);
        bp = database.create(bp);
        testBoardPartLeafs.put(bp.getLeafNumber(), bp);

        bp = createBoardPart("Next", 4);
        bp.setOrderIndex(1);
        bp.setLeafNumber(1);
        bp = database.create(bp);
        testBoardPartLeafs.put(bp.getLeafNumber(), bp);

        BoardPart bpD = createBoardPart("Development", 6);
        bpD.setOrderIndex(2);
        bpD = database.create(bpD);



        bp = createBoardPart("Analysis & Design", 0);
        bp.setOrderIndex(0);
        bp.setLeafNumber(2);
        bp.setParent(bpD);
        bp = database.create(bp);
        testBoardPartLeafs.put(bp.getLeafNumber(), bp);

        bp = createBoardPart("Coding", 0);
        bp.setOrderIndex(1);
        bp.setLeafNumber(3);
        bp.setParent(bpD);
        bp = database.create(bp);
        testBoardPartLeafs.put(bp.getLeafNumber(), bp);

        bp = createBoardPart("Testing", 0);
        bp.setOrderIndex(2);
        bp.setLeafNumber(4);
        bp.setParent(bpD);
        bp = database.create(bp);
        testBoardPartLeafs.put(bp.getLeafNumber(), bp);

        bp = createBoardPart("Integration", 0);
        bp.setOrderIndex(3);
        bp.setLeafNumber(5);
        bp.setParent(bpD);
        bp = database.create(bp);
        testBoardPartLeafs.put(bp.getLeafNumber(), bp);

        bp = createBoardPart("Documentation", 0);
        bp.setOrderIndex(4);
        bp.setLeafNumber(6);
        bp.setParent(bpD);
        bp = database.create(bp);
        testBoardPartLeafs.put(bp.getLeafNumber(), bp);



        bp = createBoardPart("Acceptance ready", 4);
        bp.setOrderIndex(3);
        bp.setLeafNumber(7);
        bp = database.create(bp);
        testBoardPartLeafs.put(bp.getLeafNumber(), bp);

        bp = createBoardPart("Acceptance", 2);
        bp.setOrderIndex(4);
        bp.setLeafNumber(8);
        bp = database.create(bp);
        testBoardPartLeafs.put(bp.getLeafNumber(), bp);

        bp = createBoardPart("Done", 0);
        bp.setOrderIndex(5);
        bp.setLeafNumber(9);
        bp = database.create(bp);
        testBoardPartLeafs.put(bp.getLeafNumber(), bp);


        testBoard = database.update(testBoard);
    }

    private Project createProject() throws DatabaseException {
        Project p = new Project();
        p.setCode(FAKER.app.name().toLowerCase().replaceAll(" ", "-"));
        p.setName(FAKER.app.name());
        p.setProductBuyer(FAKER.company.name());
        p.setDescription(FAKER.lorem.characters(150));
        p.setStartDate(FAKER.date.forward(1));
        p.setEndDate(FAKER.date.forward(FAKER.number.between(40, 100)));

        p.setOwner(kanbanMasterAccount);
        p.setDevTeam(testDevTeam);

        p.setBoard(testBoard);

        p = database.create(p);

        return p;
    }

    private void generateProjects() throws DatabaseException {
        testProject1 = createProject();
        testProject2 = createProject();
    }



    private void createCards(Project project, ArrayList<Card> testCards) throws Exception {
        database.getEntityManager().flush();

        BoardPart firstColumn = testBoardPartLeafs.get(0);
        firstColumn.setCards(new HashSet<>());

        BoardPart highestPriorityColumn = testBoardPartLeafs.get(1);
        highestPriorityColumn.setCards(new HashSet<>());

        for(int i=0; i<10; i++) {

            Card c = new Card();

            c.setName(FAKER.app.name());
            c.setCode(FAKER.app.name());
            c.setCardType(CardType.values()[FAKER.number.between(0, 2)]);
            c.setDescription(FAKER.lorem.words(30).stream().collect(Collectors.joining(" ")));
            c.setWorkload(FAKER.number.between(1, 10));

            c.setProject(project);
            c.setRejected(false);


            if(i == 0) {
                c.setSilverBullet(true);
                c.setColor("#C0C0C0");
                c.setBoardPart(highestPriorityColumn);
                c = cardService.create(c, kanbanMasterAccount);
                highestPriorityColumn.getCards().add(c);
            } else {
                c.setSilverBullet(false);
                c.setColor("#df541e");
                c.setBoardPart(firstColumn);
                c = cardService.create(c, productOwnerAccount);
                firstColumn.getCards().add(c);
            }

            testCards.add(c);

            int tasks = FAKER.number.between(0,3);
            for(int t=0; t<tasks; t++){
                SubTask st = new SubTask();
                st.setName(FAKER.app.name());
                st.setDescription(FAKER.lorem.words(10).stream().collect(Collectors.joining(" ")));
                st.setWorkingHours(FAKER.number.between(5, 40));
                st.setCard(c);

                database.create(st);
            }
        }
    }

    private void generateCards() throws Exception {
        testCards1 = new ArrayList<>();
        createCards(testProject1, testCards1);

        testCards2 = new ArrayList<>();
        createCards(testProject2, testCards2);
    }

    private void generateRequests() throws Exception {

        Request request = new Request();
        request.setRequestType(RequestType.KANBAN_MASTER_INVITE);
        request.setContext("Invite to become kanban master for dev team " + testDevTeam.getName());
        request.setRequestStatus(RequestStatus.PENDING);
        request.setSender(kanbanMasterAccount);
        request.setReceiver(testAccount);
        request.setReferenceId(testDevTeam.getId());

        database.create(request);
    }


    private int nextMove(double total, double completed) {
        if(Math.random() < 0.20) {
            return 0; // no movement
        } else {
            double k = (total - completed) / total;
            if(Math.random() <= k) {
                return 1; // move right
            } else {
                return -1; // move left
            }
        }
    }

    private void generateCardMove(ArrayList<Card> testCards) throws Exception {

        int hp = testBoard.getHighestPriority();
        int sd = testBoard.getStartDev();
        int ed = testBoard.getEndDev();
        int at = testBoard.getAcceptanceTesting();

        int completed = 0;

        for(Card c : testCards) {
            database.getEntityManager().flush();

            int dayBefore = 12 + (int)(Math.random() * 4);

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, -dayBefore);

            c.setCreatedOn(cal.getTime());
            c.setEditedOn(cal.getTime());
            for(CardMove cm : c.getCardMoves()) {
                cm.setCreatedOn(cal.getTime());
                cm.setEditedOn(cal.getTime());
            }

            for(int d=0; d<dayBefore; d++) {
                int column = c.getBoardPart().getLeafNumber();

                if(column == testBoardPartLeafs.size() - 1) {
                    completed++;
                    break;
                }

                cal.add(Calendar.DATE, 1);

                CardMove cm = new CardMove();
                cm.setCard(c);

                if(column == at) {
                    if(Math.random() > 0.2) {
                        int rColumn = column + 1;
                        BoardPart right = testBoardPartLeafs.get(rColumn);
                        cm.setTo(right);

                        cm = cardMoveService.create(cm, productOwnerAccount);
                        cm.setCreatedOn(cal.getTime());
                        cm.setEditedOn(cal.getTime());
                    } else {
                        BoardPart colHp = testBoardPartLeafs.get(hp);
                        cm.setTo(colHp);

                        cm = cardMoveService.create(cm, productOwnerAccount);
                        cm.setCreatedOn(cal.getTime());
                        cm.setEditedOn(cal.getTime());
                    }
                } else {
                    int next = nextMove(testCards.size(), completed);
                    if(next == 1 && column < testBoardPartLeafs.size() - 1) {
                        int rColumn = column + 1;
                        BoardPart right = testBoardPartLeafs.get(rColumn);
                        cm.setTo(right);

                        UserAccount authUser = kanbanMasterAccount;
                        if(column < hp) {
                            authUser = productOwnerAccount;
                        } else if(sd - 1 <= column && column <= ed) {
                            authUser = developerAccount;
                        } else if(column >= at) {
                            authUser = productOwnerAccount;
                        }

                        cm = cardMoveService.create(cm, authUser);
                        cm.setCreatedOn(cal.getTime());
                        cm.setEditedOn(cal.getTime());
                    } else if(next == -1 && column > 0 && column <= at) {
                        int lColumn = column - 1;
                        BoardPart left = testBoardPartLeafs.get(lColumn);
                        cm.setTo(left);

                        UserAccount authUser = kanbanMasterAccount;
                        if(column <= hp) {
                            authUser = productOwnerAccount;
                        } else if(sd + 1 <= column && column <= ed) {
                            authUser = developerAccount;
                        } else if(column >= at) {
                            continue;
                        }

                        cm = cardMoveService.create(cm, authUser);
                        cm.setCreatedOn(cal.getTime());
                        cm.setEditedOn(cal.getTime());
                    }
                }
            }

        }

    }

    private void generateCardMoves() throws Exception {
        generateCardMove(testCards1);
        generateCardMove(testCards2);
    }

}
