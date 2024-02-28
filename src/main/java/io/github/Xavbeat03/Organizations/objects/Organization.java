package io.github.Xavbeat03.Organizations.objects;

import com.palmergames.bukkit.towny.TownyAPI;
import org.bukkit.entity.Player;

import java.util.*;

public class Organization {
    private static final int NAME_MAX_CHAR_LENGTH = 40;
    private static final int NAME_MIN_CHAR_LENGTH = 3;
    private static final int MOTD_MAX_CHAR_LENGTH = 100;
    private static final int DESCRIPTION_MAX_CHAR_LENGTH = 255;
    private static final int LOGO_WIDTH = 15;
    private static final int LOGO_HEIGHT = 15;
    private static final int LOGO_MAX_CHAR_LENGTH = LOGO_HEIGHT * (LOGO_WIDTH + 1);
    private static final int RANK_MAX_CHAR_LENGTH = 50;
    private static final int MAX_NUM_OF_RANKS = 10;
    private static final int MAX_SUB_ORGANIZATION_COUNT = 5;


    private static int organizationCount = 0;
    private static Map<Integer, Organization> organizationList = new HashMap<>();
    private static Map<UUID, Organization> organizationListByUUID = new HashMap<>();
    private int organizationTempId = organizationCount++;
    private String name;
    private String motd;
    private String description;
    private String logo;
    private Date foundingDate;
    private UUID uuid;
    private Map<Integer, String> ranksMap = new HashMap<>();
    private List<UUID> subOrganizations;
    private UUID parentOrganization;
    private Map<UUID, Integer> playerUUIDRankMap;
    private List<UUID> explicitlyJoinedPlayers;
    private List<UUID> joinedTownUUIDs;
    private List<UUID> joinedNationUUIDs;

    /**
     * Instantiates a new Organization.
     * @param name the name of the organization
     * @param foundingDate the founding date of the organization
     * @param player the player who is creating the organization
     */
    public Organization(String name, Date foundingDate, Player player){
        if(name.length() <= NAME_MAX_CHAR_LENGTH && name.length() > NAME_MIN_CHAR_LENGTH) throw new IllegalArgumentException("Name of organization is not contained within [%d,%d]".formatted(NAME_MIN_CHAR_LENGTH, NAME_MAX_CHAR_LENGTH));

        checkIfNameIsBeingUsed(name);
        this.name = name;
        this.motd = "";
        this.description = "";
        this.logo = "";
        this.foundingDate = foundingDate;
        ranksMap.put(0, "Member");
        ranksMap.put(9, "Leader");
        this.subOrganizations = new ArrayList<>();
        this.parentOrganization = null;
        this.playerUUIDRankMap.put(player.getUniqueId(), 9);
        this.explicitlyJoinedPlayers.add(player.getUniqueId());
        this.joinedTownUUIDs = new ArrayList<>();
        this.joinedNationUUIDs = new ArrayList<>();

        Organization.organizationList.put(this.organizationTempId, this);
        Organization.organizationListByUUID.put(this.uuid, this);
    }

    /**
     * Instantiates a new Organization.
     * @param name the name of the organization
     * @param motd the message of the day for the organization
     * @param description the description of the organization
     * @param logo the logo of the organization
     * @param foundingDate the founding date of the organization
     * @param uuid the uuid of the organization
     * @param ranksMap the ranks map of the organization
     * @param subOrganizations the sub organizations of the organization
     * @param parentOrganization the parent organization of the organization
     * @param joinedTownUUIDs the joined town uuids of the organization
     * @param joinedNationUUIDs the joined nation uuids of the organization
     */
    public Organization(String name, String motd, String description,
                        String logo, Date foundingDate, UUID uuid,
                        Map<Integer, String> ranksMap, List<UUID> subOrganizations, UUID parentOrganization,
                        Map<UUID, Integer> playerUUIDsRankMap, List<UUID> joinedTownUUIDs, List<UUID> joinedNationUUIDs) {
        checkIfNameIsBeingUsed(name);
        this.name = name;
        this.motd = motd;
        this.description = description;
        this.logo = logo;
        this.foundingDate = foundingDate;
        this.uuid = uuid;
        this.ranksMap = ranksMap;
        this.subOrganizations = subOrganizations;
        this.parentOrganization = parentOrganization;
		this.joinedTownUUIDs = joinedTownUUIDs;
        this.joinedNationUUIDs = joinedNationUUIDs;
    }

    /**
     * Sets the name of the organization.
     * @param name the name of the organization
     */
    public void setName(String name){
        if(name.length() <= NAME_MAX_CHAR_LENGTH && name.length() > NAME_MIN_CHAR_LENGTH) throw new IllegalArgumentException("Name of organization is not contained within [%d,%d].".formatted(NAME_MIN_CHAR_LENGTH, NAME_MAX_CHAR_LENGTH));
        checkIfNameIsBeingUsed(name);
        this.name = name;
    }

    private static void checkIfNameIsBeingUsed(String name) throws IllegalArgumentException {
        Organization.organizationList.values().forEach( o -> {if (name.equals(o.name)) throw new IllegalArgumentException("Name of organization is already contained within the Organization Map.");});
    }

    /**
     * Sets the message of the day for the organization.
     * @param motd the message of the day for the organization
     */
    public void setMotd(String motd) {
        if(motd.length() > MOTD_MAX_CHAR_LENGTH) throw new IllegalArgumentException("Message of the day is not contained within [0,%d].".formatted(MOTD_MAX_CHAR_LENGTH));
        this.motd = motd;
    }

    /**
     * Sets the description of the organization.
     * @param description the description of the organization
     */
    public void setDescription(String description) {
        if (description.length() > DESCRIPTION_MAX_CHAR_LENGTH)
            throw new IllegalArgumentException("Description is not contained within [0,%d].".formatted(DESCRIPTION_MAX_CHAR_LENGTH));
        this.description = description;
    }

    /**
     * Sets the logo of the organization.
     * @param logo the logo of the organization
     */
    public void setLogo(String logo) {
        if (logo.length() > LOGO_MAX_CHAR_LENGTH)
            throw new IllegalArgumentException("Logo is not contained within [0,%d].".formatted(LOGO_MAX_CHAR_LENGTH));
        String[] logoLines = logo.split("\n");
        if(logoLines.length >= LOGO_HEIGHT)
            throw new IllegalArgumentException("Logo has more lines then %d.".formatted(LOGO_HEIGHT));
        for(String line: logoLines){
            int i = -1;
            i++;
            if (line.length() > 15){
                throw new IllegalArgumentException("Logo line %d is longer then %d.".formatted(i, LOGO_WIDTH));
            }
        }
        this.logo = logo;
    }

    /**
     * Adds a rank to the organization.
     * @param rankId the id of the rank to add
     * @param rankName the name of the rank to add
     */
    public void addRank(Integer rankId, String rankName){
        updateRanksMap(rankId, rankName);
    }

    /**
     * Removes a rank from the organization.
     * @param rankId the id of the rank to remove
     */
    public void removeRank(Integer rankId){
        updateRanksMap(rankId, "");
    }

    /**
     * Updates a rank in the organization.
     * @param i the id of the rank to update
     * @param s the name of the rank to update
     */
    private void updateRanksMap(Integer i, String s){
        if (i < 0 || i >= MAX_NUM_OF_RANKS) throw new IllegalArgumentException("Rank id is outside of bounds [0,%d]".formatted(MAX_NUM_OF_RANKS-1));
        if (s.length() > RANK_MAX_CHAR_LENGTH) throw new IllegalArgumentException("Rank length is longer then %d".formatted(RANK_MAX_CHAR_LENGTH));
        this.ranksMap.put(i, s);
    }

    /**
     * Adds a sub organization to the organization.
     * @param subOrganization the sub organization to add
     */
    public void addSubOrganization(UUID subOrganization){
        if(this.uuid == subOrganization) throw new IllegalArgumentException("Organization cannot be its own suborganization.");
        if(subOrganization == this.parentOrganization) throw new IllegalArgumentException("Suborganization cannot be its own parent organization.");
        if(this.subOrganizations.contains(subOrganization)) throw new IllegalArgumentException("SubOrganization already contained by Organization.");
        if(this.subOrganizations.size() > MAX_SUB_ORGANIZATION_COUNT) throw new IllegalArgumentException("Already have %d Sub Organizations contained within Organization.".formatted(this.subOrganizations.size()));
        this.subOrganizations.add(subOrganization);
    }

    /**
     * Removes a sub organization from the organization.
     * @param subOrganization the sub organization to remove
     */
    public void removeSubOrganization(UUID subOrganization){
        if(!this.subOrganizations.contains(subOrganization)) throw new IllegalArgumentException("SubOrganization not contained by Organization.");
        this.subOrganizations.remove(subOrganization);
    }

    /**
     * Updates the parent organization of the organization.
     * @param parentOrganization the parent organization to update
     */
    public void updateParentOrganization(UUID parentOrganization) {
        if(this.uuid == parentOrganization) throw new IllegalArgumentException("Organization cannot be its own parent organization.");
        if(this.parentOrganization == parentOrganization) throw new IllegalArgumentException("Parent organization already is Organization's parent.");
        this.parentOrganization = parentOrganization;
    }

    /**
     * Adds a player to the organization.
     * @param playerUUID the player to add
     * TODO: Have some param for adding explicit players
     */
    public void addPlayerToOrganization(UUID playerUUID) {
        if (this.playerUUIDRankMap.containsKey(playerUUID))
            throw new IllegalArgumentException("Player already contained by Organization.");
        this.playerUUIDRankMap.put(playerUUID, 0);
    }

    /**
     * Adds players to the organization.
     * @param playerUUIDs the players to add
     */
    public void addPlayersToOrganization(List<UUID> playerUUIDs) {
        for(UUID playerUUID: playerUUIDs){
            addPlayerToOrganization(playerUUID);
        }
    }

    /**
     * Removes a player from the organization.
     * @param playerUUID the player to remove
     */
    public void removePlayerFromOrganization(UUID playerUUID) {
        if (!this.playerUUIDRankMap.containsKey(playerUUID))
            throw new IllegalArgumentException("Player not contained by Organization.");
        this.playerUUIDRankMap.remove(playerUUID);
    }

    /**
     * Removes players from the organization.
     * @param playerUUIDs the players to remove
     */
    public void removePlayersFromOrganization(List<UUID> playerUUIDs) {
        for(UUID playerUUID: playerUUIDs){
            removePlayerFromOrganization(playerUUID);
        }
    }

    public void addTownToOrganization(UUID townUUID){
        if(this.joinedTownUUIDs.contains(townUUID))
            throw new IllegalArgumentException("Town already contained by Organization.");
        if(TownyAPI.getInstance().getTown(townUUID) == null)
            throw new IllegalArgumentException("Town doesn't exist.");
        this.joinedTownUUIDs.add(townUUID);
        TownyAPI.getInstance().getTown(townUUID).getResidents().forEach(resident -> addPlayerToOrganization(resident.getUUID()));
    }



    /**
     * Gets an organization by UUID
     * @param organizationUUID the UUID of the organization to get
     * @return the organization
     */
    public static Organization getOrganizationByUUID(UUID organizationUUID){
        return Organization.organizationListByUUID.get(organizationUUID);
    }

    /**
     * Gets organization count.
     *
     * @return the organization count
     */
    public static int getOrganizationCount() {
        return organizationCount;
    }

    /**
     * Gets organization list.
     *
     * @return the organization list
     */
    public static Map<Integer, Organization> getOrganizationList() {
        return organizationList;
    }

    /**
     * Gets organization by id.
     *
     * @return the organization by id
     */
    public int getOrganizationTempId() {
        return organizationTempId;
    }

    /**
     * Gets organization by id.
     *
     * @return the name of the organization
     */
    public String getName() {
        return name;
    }

    /**
     * Gets motd.
     *
     * @return the motd
     */
    public String getMotd() {
        return motd;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets logo.
     *
     * @return the logo
     */
    public String getLogo() {
        return logo;
    }

    /**
     * Gets founding date.
     *
     * @return the founding date
     */
    public Date getFoundingDate() {
        return foundingDate;
    }

    /**
     * Gets uuid.
     *
     * @return the uuid
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Gets ranks map.
     *
     * @return the ranks map
     */
    public Map<Integer, String> getRanksMap() {
        return ranksMap;
    }

    /**
     * Gets sub organizations.
     *
     * @return the sub organizations
     */
    public List<UUID> getSubOrganizations() {
        return subOrganizations;
    }

    /**
     * Gets parent organization.
     *
     * @return the parent organization
     */
    public UUID getParentOrganization() {
        return parentOrganization;
    }

    /**
     * Gets the player UUID rank map
     *
     * @return the player uuid rank map
     */
    public Map<UUID, Integer> getPlayerUUIDRankMap() {return playerUUIDRankMap;}

    /**
     * Gets joined town uuids.
     *
     * @return the joined town uuids
     */
    public List<UUID> getJoinedTownUUIDs() {
        return joinedTownUUIDs;
    }

    /**
     * Gets joined nation uuids.
     *
     * @return the joined nation uuids
     */
    public List<UUID> getJoinedNationUUIDs() {
        return joinedNationUUIDs;
    }
}
